import { Component, Inject, inject, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar, MatSnackBarRef, SimpleSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { ConfirmComponent } from 'src/app/modules/shared/components/confirm/confirm.component';
import { ResponsableService } from 'src/app/modules/shared/services/responsable.service';
import { UtilService } from 'src/app/modules/shared/services/util.service';
import { NewResponsableComponent } from '../new-responsable/new-responsable.component';

@Component({
  selector: 'app-responsable',
  templateUrl: './responsable.component.html',
  styleUrls: ['./responsable.component.css']
})
export class ResponsableComponent implements OnInit{

  isAdmin: any;
  private responsableService = inject(ResponsableService);
  private snackBar = inject(MatSnackBar);
  public dialog = inject(MatDialog);
  private util = inject (UtilService);

  ngOnInit(): void {
    this.getResponsablesss();
    console.log(this.util.getRoles());
    this.isAdmin = this.util.isAdmin();
  }

  displayedColumns: string[] = ['id', 'arearesponsable', 'nombresyapellidos', 'actions'];
  dataSource = new MatTableDataSource<ResponsableElement>();

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  getResponsablesss(): void {
    this.responsableService.getResponsables()
      .subscribe( (data:any) => {
        console.log("respuesta Responsables: ", data);
        this.processResponsablesResponse(data);
      }, (error: any) => {
        console.log("error: ", error);
      })
  }

  processResponsablesResponse(resp: any){

    const dataResponsable: ResponsableElement[] = [];

    if( resp.metadata[0].code == "00") {

      let listResponsable = resp.responsableResponse.listaresponsables;

      listResponsable.forEach((element: ResponsableElement) => {
        dataResponsable.push(element);
      });

      this.dataSource = new MatTableDataSource<ResponsableElement>(dataResponsable);
      this.dataSource.paginator = this.paginator;
      
    }

  }

  openResponsableDialog(){
    const dialogRef = this.dialog.open(NewResponsableComponent , {
      width: '450px'
    });

    dialogRef.afterClosed().subscribe((result:any) => {
      
      if( result == 1){
        this.openSnackBar("Responsable agregado", "Éxito");
        this.getResponsablesss();
      } else if (result == 2) {
        this.openSnackBar("Se produjo un error al guardar categoria", "Error");
      }
    });
  }

  edit(id:number, arearesponsable: string, nombresyapellidos: string){
    const dialogRef = this.dialog.open(NewResponsableComponent , {
      width: '450px',
      data: {id: id, arearesponsable: arearesponsable, nombresyapellidos: nombresyapellidos}
    });

    dialogRef.afterClosed().subscribe((result:any) => {
      
      if( result == 1){
        this.openSnackBar("Responsable actualizado", "Éxito");
        this.getResponsablesss();
      } else if (result == 2) {
        this.openSnackBar("Se produjo un error al actualizar el responsable", "Error");
      }
    });
  }

  delete(id: any){
    const dialogRef = this.dialog.open(ConfirmComponent , {
      data: {id: id, module: "Responsable"}
    });

    dialogRef.afterClosed().subscribe((result:any) => {
      
      if( result == 1){
        this.openSnackBar("Responsable eliminado", "Éxito");
        this.getResponsablesss();
      } else if (result == 2) {
        this.openSnackBar("Se produjo un error al eliminar categoria", "Error");
      }
    });
  }

  buscar( termino: string){

    if( termino.length === 0){
      return this.getResponsablesss();
    }

    this.responsableService.getResponsableById(termino)
            .subscribe( (resp: any) => {
              this.processResponsablesResponse(resp);
            })
  }

  openSnackBar(message: string, action: string) : MatSnackBarRef<SimpleSnackBar>{
    return this.snackBar.open(message, action, {
      duration: 2000
    })

  }

  exportExcel(){

    this.responsableService.exportResponsables()
        .subscribe( (data: any) => {
          let file = new Blob([data], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'});
          let fileUrl = URL.createObjectURL(file);
          var anchor = document.createElement("a");
          anchor.download = "Responsables.xlsx";
          anchor.href = fileUrl;
          anchor.click();

          this.openSnackBar("Archivo exportado correctamente", "Exitosa");
        }, (error: any) =>{
          this.openSnackBar("No se pudo exportar el archivo", "Error");
        })

  }

}

export interface ResponsableElement {
  nombresyapellidos: string;
  id: number;
  arearesponsable: string;
}

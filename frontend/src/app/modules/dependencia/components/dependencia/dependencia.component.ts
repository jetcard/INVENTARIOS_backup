import { Component, Inject, inject, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar, MatSnackBarRef, SimpleSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { ConfirmComponent } from 'src/app/modules/shared/components/confirm/confirm.component';
import { DependenciaService } from 'src/app/modules/shared/services/dependencia.service';
import { UtilService } from 'src/app/modules/shared/services/util.service';
import { NewDependenciaComponent } from '../new-dependencia/new-dependencia.component';

@Component({
  selector: 'app-dependencia',
  templateUrl: './dependencia.component.html',
  styleUrls: ['./dependencia.component.css']
})
export class DependenciaComponent implements OnInit{

  isAdmin: any;
  private dependenciaService = inject(DependenciaService);
  private snackBar = inject(MatSnackBar);
  public dialog = inject(MatDialog);
  private util = inject (UtilService);

  ngOnInit(): void {
    this.getDependenciass();
    console.log(this.util.getRoles());
    this.isAdmin = this.util.isAdmin();
  }

  displayedColumns: string[] = ['id', 'nombredependencia', 'descripdependencia', 'actions'];
  dataSource = new MatTableDataSource<DependenciaElement>();

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  getDependenciass(): void {

    this.dependenciaService.getDependencias()
      .subscribe( (data:any) => {

        console.log("respuesta dependencias: ", data);
        this.processDependenciasResponse(data);

      }, (error: any) => {
        console.log("error: ", error);
      })
  }

  processDependenciasResponse(resp: any){

    const dataDependencia: DependenciaElement[] = [];

    if( resp.metadata[0].code == "00") {

      let listDependencia = resp.dependenciaResponse.listadependencias;

      listDependencia.forEach((element: DependenciaElement) => {
        dataDependencia.push(element);
      });

      this.dataSource = new MatTableDataSource<DependenciaElement>(dataDependencia);
      this.dataSource.paginator = this.paginator;
      
    }

  }

  openDependenciaDialog(){
    const dialogRef = this.dialog.open(NewDependenciaComponent , {
      width: '450px'
    });

    dialogRef.afterClosed().subscribe((result:any) => {
      
      if( result == 1){
        this.openSnackBar("Dependencia agregada", "Éxito");
        this.getDependenciass();
      } else if (result == 2) {
        this.openSnackBar("Se produjo un error al guardar la dependencia", "Error");
      }
    });
  }

  edit(id:number, nombredependencia: string, descripdependencia: string){
    const dialogRef = this.dialog.open(NewDependenciaComponent , {
      width: '450px',
      data: {id: id, nombredependencia: nombredependencia, descripdependencia: descripdependencia}
    });

    dialogRef.afterClosed().subscribe((result:any) => {
      
      if( result == 1){
        this.openSnackBar("Dependencia Actualizado", "Éxito");
        this.getDependenciass();
      } else if (result == 2) {
        this.openSnackBar("Se produjo un error al actualizar el dependencia", "Error");
      }
    });
  }

  delete(id: any){
    const dialogRef = this.dialog.open(ConfirmComponent , {
      data: {id: id, module: "dependencia"}
    });

    dialogRef.afterClosed().subscribe((result:any) => {
      
      if( result == 1){
        this.openSnackBar("Dependencia Eliminada", "Exitosa");
        this.getDependenciass();
      } else if (result == 2) {
        this.openSnackBar("Se produjo un error al eliminar la dependencia", "Error");
      }
    });
  }

  buscar( termino: string){

    if( termino.length === 0){
      return this.getDependenciass();
    }

    this.dependenciaService.getDependenciaByModelo(termino)
            .subscribe( (resp: any) => {
              this.processDependenciasResponse(resp);
            })
  }

  openSnackBar(message: string, action: string) : MatSnackBarRef<SimpleSnackBar>{
    return this.snackBar.open(message, action, {
      duration: 2000
    })

  }

  exportExcel(){
    this.dependenciaService.exportDependencia()
        .subscribe( (data: any) => {
          let file = new Blob([data], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'});
          let fileUrl = URL.createObjectURL(file);
          var anchor = document.createElement("a");
          anchor.download = "dependencias.xlsx";
          anchor.href = fileUrl;
          anchor.click();

          this.openSnackBar("Archivo exportado correctamente", "Exitosa");
        }, (error: any) =>{
          this.openSnackBar("No se pudo exportar el archivo", "Error");
        })

  }

}

export interface DependenciaElement {
  descripdependencia: string;
  id: number;
  nombredependencia: string;
  
}

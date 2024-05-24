import { Component, Inject, inject, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar, MatSnackBarRef, SimpleSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { ConfirmComponent } from 'src/app/modules/shared/components/confirm/confirm.component';
import { GrupoService } from 'src/app/modules/shared/services/grupo.service';
import { UtilService } from 'src/app/modules/shared/services/util.service';
import { NewGrupoComponent } from '../new-grupo/new-grupo.component';

@Component({
  selector: 'app-grupo',
  templateUrl: './grupo.component.html',
  styleUrls: ['./grupo.component.css']
})
export class GrupoComponent implements OnInit{

  isAdmin: any;
  private grupoService = inject(GrupoService);
  private snackBar = inject(MatSnackBar);
  public dialog = inject(MatDialog);
  private util = inject (UtilService);

  ngOnInit(): void {
    this.getGruposs();
    console.log(this.util.getRoles());
    this.isAdmin = this.util.isAdmin();
  }

  displayedColumns: string[] = ['id', 'nombregrupo', 'descripgrupo', 'actions'];
  dataSource = new MatTableDataSource<GrupoElement>();

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  getGruposs(): void {

    this.grupoService.getGrupos()
      .subscribe( (data:any) => {

        console.log("respuesta grupos: ", data);
        this.processGruposResponse(data);

      }, (error: any) => {
        console.log("error: ", error);
      })
  }

  processGruposResponse(resp: any){
    const dataGrupo: GrupoElement[] = [];
    if( resp.metadata[0].code == "00") {
      let listGrupo = resp.grupoResponse.listagrupos;
      listGrupo.forEach((element: GrupoElement) => {
        dataGrupo.push(element);
      });
      this.dataSource = new MatTableDataSource<GrupoElement>(dataGrupo);
      this.dataSource.paginator = this.paginator;
    }
  }

  openGrupoDialog(){
    const dialogRef = this.dialog.open(NewGrupoComponent , {
      width: '450px'
    });

    dialogRef.afterClosed().subscribe((result:any) => {
      
      if( result == 1){
        this.openSnackBar("Grupo Agregado", "Éxito");
        this.getGruposs();
      } else if (result == 2) {
        this.openSnackBar("Se produjo un error al guardar el grupo", "Error");
      }
    });
  }

  edit(id:number, nombregrupo: string, descripgrupo: string){
    const dialogRef = this.dialog.open(NewGrupoComponent , {
      width: '450px',
      data: {id: id, nombregrupo: nombregrupo, descripgrupo: descripgrupo}
    });

    dialogRef.afterClosed().subscribe((result:any) => {
      
      if( result == 1){
        this.openSnackBar("Grupo Actualizado", "Éxito");
        this.getGruposs();
      } else if (result == 2) {
        this.openSnackBar("Se produjo un error al actualizar el grupo", "Error");
      }
    });
  }

  delete(id: any){
    const dialogRef = this.dialog.open(ConfirmComponent , {
      data: {id: id, module: "grupo"}
    });

    dialogRef.afterClosed().subscribe((result:any) => {
      
      if( result == 1){
        this.openSnackBar("Grupo Eliminado", "Exitosa");
        this.getGruposs();
      } else if (result == 2) {
        this.openSnackBar("Se produjo un error al eliminar el grupo", "Error");
      }
    });
  }

  buscar( termino: string){

    if( termino.length === 0){
      return this.getGruposs();
    }

    this.grupoService.getGrupoById(termino)
            .subscribe( (resp: any) => {
              this.processGruposResponse(resp);
            })
  }

  openSnackBar(message: string, action: string) : MatSnackBarRef<SimpleSnackBar>{
    return this.snackBar.open(message, action, {
      duration: 2000
    })

  }

  exportExcel(){
    this.grupoService.exportGrupos()
        .subscribe( (data: any) => {
          let file = new Blob([data], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'});
          let fileUrl = URL.createObjectURL(file);
          var anchor = document.createElement("a");
          anchor.download = "grupos.xlsx";
          anchor.href = fileUrl;
          anchor.click();

          this.openSnackBar("Archivo exportado correctamente", "Exitosa");
        }, (error: any) =>{
          this.openSnackBar("No se pudo exportar el archivo", "Error");
        })

  }

}

export interface GrupoElement {
  descripgrupo: string;
  id: number;
  nombregrupo: string;
  
}

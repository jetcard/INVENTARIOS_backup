import { Component, inject, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar, MatSnackBarRef, SimpleSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { ConfirmComponent } from '../../shared/components/confirm/confirm.component';
import { ComunService } from '../../shared/services/comun.service';
import { UtilService } from '../../shared/services/util.service';
import { NewComunComponent } from '../new-comun/new-comun.component';

@Component({
  selector: 'app-comun',
  templateUrl: './comun.component.html',
  styleUrls: ['./comun.component.css']
})
export class ComunComponent implements OnInit{
  
  isAdmin: any;
  private comunService = inject(ComunService);
  private snackBar = inject(MatSnackBar);
  public dialog = inject(MatDialog);
  private util = inject(UtilService);

  ngOnInit(): void {
    this.getComuns();
    this.isAdmin = this.util.isAdmin();
  }

  displayedColumns: string[] = ['id', 'responsable', 'tipo', 'grupo', 'descripcomun', 'descripcortacomun',  'actions'];
  dataSource = new MatTableDataSource<ComunElement>();

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  getComuns(){
    this.comunService.getComunes()
        .subscribe( (data:any) => {
          console.log("respuesta de comuns: ", data);
          this.processComunResponse(data);
        }, (error: any) => {
          console.log("error en comuns: ", error);
        }) 
  }

  processComunResponse(resp: any){
    const dateComun: ComunElement[] = [];
     if( resp.metadata[0].code == "00"){
       let listCComun = resp.comunResponse.listacomuns;

       listCComun.forEach((element: ComunElement) => {
         ///element.grupo = element.grupo.name;
         //element.picture = 'data:image/jpeg;base64,'+element.picture;
         dateComun.push(element);
       });

       //set the datasource
       this.dataSource = new MatTableDataSource<ComunElement>(dateComun);
       this.dataSource.paginator = this.paginator;
     }
  }

  openComunDialog(){
    const dialogRef = this.dialog.open(NewComunComponent , {
      width: '450px'
    });

    dialogRef.afterClosed().subscribe((result:any) => {
      
      if( result == 1){
        this.openSnackBar("Comun Agregado", "Éxito");
        this.getComuns();
      } else if (result == 2) {
        this.openSnackBar("Se produjo un error al guardar comun", "Error");
      }
    });
  }

  openSnackBar(message: string, action: string) : MatSnackBarRef<SimpleSnackBar>{
    return this.snackBar.open(message, action, {
      duration: 2000
    })

  }

  edit(id:number, responsable:any, tipo:any, grupo:any, descripcomun:string, descripcortacomun:string){
    const dialogRef = this.dialog.open(NewComunComponent , {
      width: '450px', 
      data: {id: id, responsable: responsable, tipo: tipo, grupo: grupo, descripcomun: descripcomun, descripcortacomun: descripcortacomun}
    });

    dialogRef.afterClosed().subscribe((result:any) => {
      if( result == 1){
        this.openSnackBar("Comun editado", "Éxito");
        this.getComuns();
      } else if (result == 2) {
        this.openSnackBar("Se produjo un error al editar comun", "Error");
      }
    });


  }

  delete(id: any){
    const dialogRef = this.dialog.open(ConfirmComponent , {
      width: '450px', 
      data: {id: id, module: "comun"}
    });

    dialogRef.afterClosed().subscribe((result:any) => {
      
      if( result == 1){
        this.openSnackBar("Comun eliminado", "Exitosa");
        this.getComuns();
      } else if (result == 2) {
        this.openSnackBar("Se produjo un error al eliminar comun", "Error");
      }
    });
  }

  buscar(modelo: any){
    if ( modelo.length === 0){
      return this.getComuns();
    }

    this.comunService.getComunByModelo(modelo)
        .subscribe( (resp: any) =>{
          this.processComunResponse(resp);
        })
  }

  exportExcel(){

    this.comunService.exportComun()
        .subscribe( (data: any) => {
          let file = new Blob([data], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'});
          let fileUrl = URL.createObjectURL(file);
          var anchor = document.createElement("a");
          anchor.download = "comuns.xlsx";
          anchor.href = fileUrl;
          anchor.click();

          this.openSnackBar("Archivo exportado correctamente", "Exitosa");
        }, (error: any) =>{
          this.openSnackBar("No se pudo exportar el archivo", "Error");
        })

  }

}

export interface ComunElement {
  id: number;
  modelo: string;
  marca: string;
  nroserie: string;
  fechaingreso: string;
  importe: number;  
  moneda: number;
  responsable: any;
  grupo: any;
  articulo: any;
  //picture: any;
}

import { Component, inject, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar, MatSnackBarRef, SimpleSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { ConfirmComponent } from '../../shared/components/confirm/confirm.component';
import { AtributoService } from '../../shared/services/atributo.service';
import { UtilService } from '../../shared/services/util.service';
import { NewAtributoComponent } from '../new-atributo/new-atributo.component';

@Component({
  selector: 'app-atributo',
  templateUrl: './atributo.component.html',
  styleUrls: ['./atributo.component.css']
})
export class AtributoComponent implements OnInit{
  
  isAdmin: any;
  private atributoService = inject(AtributoService);
  private snackBar = inject(MatSnackBar);
  public dialog = inject(MatDialog);
  private util = inject(UtilService);

  ngOnInit(): void {
    this.getAtributos();
    this.isAdmin = this.util.isAdmin();
  }

  displayedColumns: string[] = ['id', 'responsable', 'articulo', 'atributos',  'actions'];
  
  dataSource = new MatTableDataSource<AtributoElement>();

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  getAtributos(){
    this.atributoService.getAtributos()
        .subscribe( (data:any) => {
          console.log("respuesta de atributos: ", data);
          this.processAtributoResponse(data);
        }, (error: any) => {
          console.log("error en atributos: ", error);
        }) 
  }

  processAtributoResponse(resp: any){
    const dataAtributo: AtributoElement[] = [];
     if( resp.metadata[0].code == "00"){
       let listCAtributo = resp.atributoResponse.listaatributos;

       listCAtributo.forEach((element: AtributoElement) => {
         ///element.grupo = element.grupo.name;
         //element.picture = 'data:image/jpeg;base64,'+element.picture;
         dataAtributo.push(element);
       });
       this.dataSource = new MatTableDataSource<AtributoElement>(dataAtributo);
       this.dataSource.paginator = this.paginator;
     }
  }

  openAtributoDialog(){
    const dialogRef = this.dialog.open(NewAtributoComponent , {
      width: '450px'
    });

    dialogRef.afterClosed().subscribe((result:any) => {
      
      if( result == 1){
        this.openSnackBar("Atributo Agregado", "Éxito");
        this.getAtributos();
      } else if (result == 2) {
        this.openSnackBar("Se produjo un error al guardar atributo", "Error");
      }
    });
  }

  openSnackBar(message: string, action: string) : MatSnackBarRef<SimpleSnackBar>{
    return this.snackBar.open(message, action, {
      duration: 2000
    })

  }

  edit(id:number, responsable: any, articulo: any, atributos: any){
    const dialogRef = this.dialog.open(NewAtributoComponent , {
      width: '450px', 
      data: {id: id, responsable: responsable, articulo: articulo, atributos: atributos}
    });

    dialogRef.afterClosed().subscribe((result:any) => {
      if( result == 1){
        this.openSnackBar("Atributo editado", "Éxito");
        this.getAtributos();
      } else if (result == 2) {
        this.openSnackBar("Se produjo un error al editar atributo", "Error");
      }
    });


  }

  delete(id: any){
    const dialogRef = this.dialog.open(ConfirmComponent , {
      width: '450px', 
      data: {id: id, module: "atributo"}
    });

    dialogRef.afterClosed().subscribe((result:any) => {
      
      if( result == 1){
        this.openSnackBar("Atributo eliminado", "Exitosa");
        this.getAtributos();
      } else if (result == 2) {
        this.openSnackBar("Se produjo un error al eliminar atributo", "Error");
      }
    });
  }

  buscar(modelo: any){
    if ( modelo.length === 0){
      return this.getAtributos();
    }

    this.atributoService.getAtributoByModelo(modelo)
        .subscribe( (resp: any) =>{
          this.processAtributoResponse(resp);
        })
  }

  exportExcel(){

    this.atributoService.exportAtributo()
        .subscribe( (data: any) => {
          let file = new Blob([data], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'});
          let fileUrl = URL.createObjectURL(file);
          var anchor = document.createElement("a");
          anchor.download = "atributos.xlsx";
          anchor.href = fileUrl;
          anchor.click();

          this.openSnackBar("Archivo exportado correctamente", "Exitosa");
        }, (error: any) =>{
          this.openSnackBar("No se pudo exportar el archivo", "Error");
        })

  }

}

export interface AtributoElement {
  id: number;
  responsable: any;
  articulo: any;
  atributos: any;

  //picture: any;
}

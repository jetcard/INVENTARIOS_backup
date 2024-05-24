import { Component, Inject, inject, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar, MatSnackBarRef, SimpleSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { ConfirmComponent } from 'src/app/modules/shared/components/confirm/confirm.component';
import { TipoBienService } from 'src/app/modules/shared/services/tipobien.service';
import { UtilService } from 'src/app/modules/shared/services/util.service';
import { NewTipoBienComponent } from '../new-tipobien/new-tipobien.component';

@Component({
  selector: 'app-tipobien',
  templateUrl: './tipobien.component.html',
  styleUrls: ['./tipobien.component.css']
})
export class TipoBienComponent implements OnInit{

  isAdmin: any;
  private tipoBienService = inject(TipoBienService);
  private snackBar = inject(MatSnackBar);
  public dialog = inject(MatDialog);
  private util = inject (UtilService);

  ngOnInit(): void {
    this.getTipoBienes();
    console.log(this.util.getRoles());
    this.isAdmin = this.util.isAdmin();
  }

  displayedColumns: string[] = ['id', 'nombretipo', 'descriptipo', 'actions'];
  dataSource = new MatTableDataSource<TipoBienElement>();

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  getTipoBienes(): void {

    this.tipoBienService.getTipoBienes()
      .subscribe( (data:any) => {

        console.log("respuesta TipoBienes: ", data);
        this.processTipoBienesResponse(data);

      }, (error: any) => {
        console.log("error: ", error);
      })
  }

  processTipoBienesResponse(resp: any){

    const dataTipoBien: TipoBienElement[] = [];

    if( resp.metadata[0].code == "00") {

      let listTipoBien = resp.tipoResponse.listatipos;

      listTipoBien.forEach((element: TipoBienElement) => {
        dataTipoBien.push(element);
      });

      this.dataSource = new MatTableDataSource<TipoBienElement>(dataTipoBien);
      this.dataSource.paginator = this.paginator;
      
    }

  }

  openTipoBienDialog(){
    const dialogRef = this.dialog.open(NewTipoBienComponent , {
      width: '450px'
    });

    dialogRef.afterClosed().subscribe((result:any) => {
      
      if( result == 1){
        this.openSnackBar("TipoBien Agregado", "Exitosa");
        this.getTipoBienes();
      } else if (result == 2) {
        this.openSnackBar("Se produjo un error al guardar tipo de bien", "Error");
      }
    });
  }

  edit(id:number, nombretipo: string, descriptipo: string){
    const dialogRef = this.dialog.open(NewTipoBienComponent , {
      width: '450px',
      data: {id: id, nombretipo: nombretipo, descriptipo: descriptipo}
    });

    dialogRef.afterClosed().subscribe((result:any) => {
      
      if( result == 1){
        this.openSnackBar("TipoBien Actualizado", "Exitosa");
        this.getTipoBienes();
      } else if (result == 2) {
        this.openSnackBar("Se produjo un error al actualizar tipo de bien", "Error");
      }
    });
  }

  delete(id: any){
    const dialogRef = this.dialog.open(ConfirmComponent , {
      data: {id: id, module: "TipoBien"}
    });

    dialogRef.afterClosed().subscribe((result:any) => {
      
      if( result == 1){
        this.openSnackBar("Tipo de bien eliminado", "Exitosa");
        this.getTipoBienes();
      } else if (result == 2) {
        this.openSnackBar("Se produjo un error al eliminar tipo de bien", "Error");
      }
    });
  }

  buscar( termino: string){

    if( termino.length === 0){
      return this.getTipoBienes();
    }

    this.tipoBienService.getTipoBienesById(termino)
            .subscribe( (resp: any) => {
              this.processTipoBienesResponse(resp);
            })
  }

  openSnackBar(message: string, action: string) : MatSnackBarRef<SimpleSnackBar>{
    return this.snackBar.open(message, action, {
      duration: 2000
    })

  }

  exportExcel(){

    this.tipoBienService.exportTipoBienes()
        .subscribe( (data: any) => {
          let file = new Blob([data], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'});
          let fileUrl = URL.createObjectURL(file);
          var anchor = document.createElement("a");
          anchor.download = "TipoBienes.xlsx";
          anchor.href = fileUrl;
          anchor.click();

          this.openSnackBar("Archivo exportado correctamente", "Exitosa");
        }, (error: any) =>{
          this.openSnackBar("No se pudo exportar el archivo", "Error");
        })

  }

}

export interface TipoBienElement {
  descriptipo: string;
  id: number;
  nombretipo: string;
}

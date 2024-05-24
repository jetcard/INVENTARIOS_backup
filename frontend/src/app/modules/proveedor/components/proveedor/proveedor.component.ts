import { Component, Inject, inject, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar, MatSnackBarRef, SimpleSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { ConfirmComponent } from 'src/app/modules/shared/components/confirm/confirm.component';
import { ProveedorService } from 'src/app/modules/shared/services/proveedor.service';
import { UtilService } from 'src/app/modules/shared/services/util.service';
import { NewProveedorComponent } from '../new-proveedor/new-proveedor.component';

@Component({
  selector: 'app-proveedor',
  templateUrl: './proveedor.component.html',
  styleUrls: ['./proveedor.component.css']
})
export class ProveedorComponent implements OnInit{

  isAdmin: any;
  private proveedorService = inject(ProveedorService);
  private snackBar = inject(MatSnackBar);
  public dialog = inject(MatDialog);
  private util = inject (UtilService);

  ngOnInit(): void {
    this.getProveedores();
    console.log(this.util.getRoles());
    this.isAdmin = this.util.isAdmin();
  }

  displayedColumns: string[] = ['id', 'nombreproveedor', 'descripproveedor', 'actions'];
  dataSource = new MatTableDataSource<ProveedorElement>();

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  getProveedores(): void {

    this.proveedorService.getProveedores()
      .subscribe( (data:any) => {

        console.log("respuesta proveedors: ", data);
        this.processProveedoresResponse(data);

      }, (error: any) => {
        console.log("error: ", error);
      })
  }

  processProveedoresResponse(resp: any){

    const dataProveedor: ProveedorElement[] = [];

    if( resp.metadata[0].code == "00") {

      let listProveedor = resp.proveedorResponse.listaproveedores;

      listProveedor.forEach((element: ProveedorElement) => {
        dataProveedor.push(element);
      });

      this.dataSource = new MatTableDataSource<ProveedorElement>(dataProveedor);
      this.dataSource.paginator = this.paginator;
      
    }

  }

  openProveedorDialog(){
    const dialogRef = this.dialog.open(NewProveedorComponent , {
      width: '450px'
    });

    dialogRef.afterClosed().subscribe((result:any) => {
      
      if( result == 1){
        this.openSnackBar("Proveedor Agregado", "Éxito");
        this.getProveedores();
      } else if (result == 2) {
        this.openSnackBar("Se produjo un error al guardar el proveedor", "Error");
      }
    });
  }

  edit(id:number, nombreproveedor: string, descripproveedor: string){
    const dialogRef = this.dialog.open(NewProveedorComponent , {
      width: '450px',
      data: {id: id, nombreproveedor: nombreproveedor, descripproveedor: descripproveedor}
    });

    dialogRef.afterClosed().subscribe((result:any) => {
      
      if( result == 1){
        this.openSnackBar("Proveedor Actualizado", "Éxito");
        this.getProveedores();
      } else if (result == 2) {
        this.openSnackBar("Se produjo un error al actualizar el proveedor", "Error");
      }
    });
  }

  delete(id: any){
    const dialogRef = this.dialog.open(ConfirmComponent , {
      data: {id: id, module: "proveedor"}
    });

    dialogRef.afterClosed().subscribe((result:any) => {
      
      if( result == 1){
        this.openSnackBar("Proveedor Eliminado", "Exitosa");
        this.getProveedores();
      } else if (result == 2) {
        this.openSnackBar("Se produjo un error al eliminar el proveedor", "Error");
      }
    });
  }

  buscar( termino: string){

    if( termino.length === 0){
      return this.getProveedores();
    }

    this.proveedorService.getProveedorById(termino)
            .subscribe( (resp: any) => {
              this.processProveedoresResponse(resp);
            })
  }

  openSnackBar(message: string, action: string) : MatSnackBarRef<SimpleSnackBar>{
    return this.snackBar.open(message, action, {
      duration: 2000
    })

  }

  exportExcel(){
    this.proveedorService.exportProveedores()
        .subscribe( (data: any) => {
          let file = new Blob([data], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'});
          let fileUrl = URL.createObjectURL(file);
          var anchor = document.createElement("a");
          anchor.download = "proveedors.xlsx";
          anchor.href = fileUrl;
          anchor.click();

          this.openSnackBar("Archivo exportado correctamente", "Exitosa");
        }, (error: any) =>{
          this.openSnackBar("No se pudo exportar el archivo", "Error");
        })

  }

}

export interface ProveedorElement {
  descripproveedor: string;
  id: number;
  nombreproveedor: string;
  
}

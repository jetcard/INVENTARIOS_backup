import { Component, inject, AfterViewInit, OnInit, ViewChild, ChangeDetectorRef } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar, MatSnackBarRef, SimpleSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { ConfirmComponent } from '../../shared/components/confirm/confirm.component';
import { ActivoService } from '../../shared/services/activo.service';
import { UtilService } from '../../shared/services/util.service';
import { NewActivoComponent } from '../new-activo/new-activo.component';
import { ResponsableService } from '../../shared/services/responsable.service';
import { MatDatepicker } from '@angular/material/datepicker';
import { FormGroup, FormControl } from '@angular/forms';

export interface Responsable{
  nombresyapellidos: string;
  id: number;
  arearesponsable: string;
}

@Component({
  selector: 'app-activo',
  templateUrl: './activo.component.html',
  styleUrls: ['./activo.component.css']
})
export class ActivoComponent implements OnInit, AfterViewInit{

  public myFormGroup!: FormGroup;

  @ViewChild('pickerDesde') pickerDesde!: MatDatepicker<Date>;
  @ViewChild('pickerHasta') pickerHasta!: MatDatepicker<Date>;

  isAdmin: any;
  private activoService = inject(ActivoService);
  private snackBar = inject(MatSnackBar);
  public dialog = inject(MatDialog);
  private util = inject(UtilService);
  private responsableService=inject(ResponsableService);
  private cdr = inject(ChangeDetectorRef);
  responsables: Responsable[]=[];
  fechaActual: Date = new Date();
  fechaMinima: Date = new Date(2024, 0, 1);


  constructor() {
    // Define los controles dentro del FormGroup
    this.myFormGroup = new FormGroup({
      desde: new FormControl(),
      hasta: new FormControl()
    });
  }
  
  ngOnInit(): void {
    this.getActivos();
    this.isAdmin = this.util.isAdmin();
    this.getResponsabless();
    this.cdr.detectChanges();
  }

  ngAfterViewInit(): void {
    this.abrirDatepickersConFechasPorDefecto();
  }

  displayedColumns: string[] = ['id', 'codinventario', 'modelo', 'marca', 'nroserie', 'fechaingreso', 'importe', 'moneda', 'grupo',  'actions'];
  //displayedColumns: string[] = ['id', 'modelo', 'marca', 'nroserie', 'fechaingreso', 'importe', 'moneda', 'grupo', 'picture',  'actions'];
  dataSource = new MatTableDataSource<ActivoElement>();

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  getActivos(){
    this.activoService.getActivos()
        .subscribe( (data:any) => {
          console.log("respuesta de activos: ", data);
          this.processActivoResponse(data);
        }, (error: any) => {
          console.log("error en activos: ", error);
        }) 
  }

  processActivoResponse(resp: any){
    const dateActivo: ActivoElement[] = [];
     if( resp.metadata[0].code == "00"){
       let listCActivo = resp.activoResponse.listaactivos;

       listCActivo.forEach((element: ActivoElement) => {
         ///element.grupo = element.grupo.name;
         //element.picture = 'data:image/jpeg;base64,'+element.picture;
         dateActivo.push(element);
       });

       //set the datasource
       this.dataSource = new MatTableDataSource<ActivoElement>(dateActivo);
       this.dataSource.paginator = this.paginator;
     }
  }

  openActivoDialog(){
    const dialogRef = this.dialog.open(NewActivoComponent , {
      width: '850px'
    });

    dialogRef.afterClosed().subscribe((result:any) => {
      
      if( result == 1){
        this.openSnackBar("Activo Agregado", "Éxito");
        this.getActivos();
      } else if (result == 2) {
        this.openSnackBar("Se produjo un error al guardar activo", "Error");
      }
    });
  }

  openSnackBar(message: string, action: string) : MatSnackBarRef<SimpleSnackBar>{
    return this.snackBar.open(message, action, {
      duration: 2000
    })

  }

  edit(id:number, codinventario:string, modelo:string, marca:string, nroserie:string, fechaingreso:string, importe:number, moneda: number, responsable: any, grupo: any, tipo: any, articulo: any){
    const dialogRef = this.dialog.open(NewActivoComponent , {
      width: '450px', 
      data: {id: id, codinventario: codinventario, modelo: modelo, marca: marca, nroserie: nroserie, fechaingreso: fechaingreso, importe: importe, moneda: moneda, responsable: responsable, grupo: grupo, tipo: tipo, articulo: articulo}
    });

    dialogRef.afterClosed().subscribe((result:any) => {
      if( result == 1){
        this.openSnackBar("Activo editado", "Éxito");
        this.getActivos();
      } else if (result == 2) {
        this.openSnackBar("Se produjo un error al editar activo", "Error");
      }
    });


  }

  delete(id: any){
    const dialogRef = this.dialog.open(ConfirmComponent , {
      width: '450px', 
      data: {id: id, module: "activo"}
    });

    dialogRef.afterClosed().subscribe((result:any) => {
      
      if( result == 1){
        this.openSnackBar("Activo eliminado", "Exitosa");
        this.getActivos();
      } else if (result == 2) {
        this.openSnackBar("Se produjo un error al eliminar activo", "Error");
      }
    });
  }


  /*buscar(id: any){
    if ( id.length === 0){
      return this.getActivos();
    }
    this.activoService.getActivoByModelo(id)
        .subscribe( (resp: any) =>{
          this.processActivoResponse(resp);
        })
  }*/

 /* buscar(codinventario: string, modelo: string, marca: string, nroserie: string, fechaingresoDesde: string, fechaingresoHasta: string) {
    if (!codinventario && !modelo && !marca && !nroserie && !fechaingresoDesde && !fechaingresoHasta) {
      return this.getActivos();
    }
  
    const fechaDesdeDate = fechaingresoDesde ? new Date(fechaingresoDesde) : null;
    const fechaHastaDate = fechaingresoHasta ? new Date(fechaingresoHasta) : null;
  
    this.activoService.getActivoBusqueda(codinventario, modelo, marca, nroserie, fechaDesdeDate, fechaHastaDate)
      .subscribe((resp: any) => {
        this.processActivoResponse(resp);
      });
  }  */

  buscar(codinventario: string, modelo: string, marca: string, nroserie: string, fechaingresoDesde: string, fechaingresoHasta: string) {
    this.cdr.detectChanges();
    codinventario = codinventario.trim(); // Eliminar espacios en blanco
    modelo = modelo.trim();
    marca = marca.trim();
    nroserie = nroserie.trim();
  
    if (!codinventario && !modelo && !marca && !nroserie && !fechaingresoDesde && !fechaingresoHasta) {
      return this.getActivos();
    }
  
    const fechaDesdeDate = fechaingresoDesde ? new Date(fechaingresoDesde) : null;
    const fechaHastaDate = fechaingresoHasta ? new Date(fechaingresoHasta) : null;
  
    if (isNaN(fechaDesdeDate?.getTime() || 0) || isNaN(fechaHastaDate?.getTime() || 0)) {
      this.openSnackBar("Fechas inválidas", "Error");
      return;
    }

    this.activoService.getActivoBusqueda(codinventario, modelo, marca, nroserie, fechaDesdeDate, fechaHastaDate)
      .subscribe((resp: any) => {
        this.processActivoResponse(resp);
      });
  }

  abrirDatepickersConFechasPorDefecto(): void {
    const fechaDesde = new Date();
    const fechaHasta = new Date();
    this.myFormGroup.controls['desde'].setValue(fechaDesde);
    this.myFormGroup.controls['hasta'].setValue(fechaHasta);

    this.cdr.detectChanges(); 

    ///this.pickerDesde.select(this.fechaMinima);
    ///this.pickerHasta.select(this.fechaActual);    
    (this.pickerDesde as any).select(this.fechaMinima);
    (this.pickerHasta as any).select(this.fechaActual); 
  }
  
  limpiarCampos(codinventario: string, modelo: string, marca: string, nroserie: string, fechaingresoDesde: string, fechaingresoHasta: string) {
    codinventario = '';
    modelo = '';
    marca = '';
    nroserie = '';
}
 

  exportExcel(){

    this.activoService.exportActivo()
        .subscribe( (data: any) => {
          let file = new Blob([data], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'});
          let fileUrl = URL.createObjectURL(file);
          var anchor = document.createElement("a");
          anchor.download = "activos.xlsx";
          anchor.href = fileUrl;
          anchor.click();

          this.openSnackBar("Archivo exportado correctamente", "Exitosa");
        }, (error: any) =>{
          this.openSnackBar("No se pudo exportar el archivo", "Error");
        })

  }

  getResponsabless(){
    this.responsableService.getResponsables()
        .subscribe( (data: any) =>{
          this.responsables = data.responsableResponse.listaresponsables;
        }, (error: any) =>{
          console.log("error al consultar responsables");
        })
  }   

}

export interface ActivoElement {
  id: number;
  codinventario: string,
  modelo: string;
  marca: string;
  nroserie: string;
  fechaingreso: Date;
  importe: number;  
  moneda: number;
  responsable: any;
  grupo: any;
  articulo: any;
  //picture: any;
}

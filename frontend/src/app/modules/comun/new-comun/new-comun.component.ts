import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ResponsableService } from '../../shared/services/responsable.service';
import { GrupoService } from '../../shared/services/grupo.service';
import { TipoBienService } from '../../shared/services/tipobien.service';
import { ComunService } from '../../shared/services/comun.service';

export interface Responsable{
  nombresyapellidos: string;
  id: number;
  arearesponsable: string;
}
export interface Grupo{
  descripgrupo: string;
  id: number;
  nombregrupo: string;
}
export interface TipoBien{
  descriptipo: string;
  id: number;
  nombretipo: string;
}
export interface Articulo{
  descriparticulo: string;
  id: number;
  nombrearticulo: string;
}
@Component({
  selector: 'app-new-comun',
  templateUrl:'./new-comun.component.html',
  styleUrls: ['./new-comun.component.css']
})
export class NewComunComponent implements OnInit{
  
  private fb = inject(FormBuilder);
  private responsableService=inject(ResponsableService);
  private grupoService= inject(GrupoService);
  private tipoService=inject(TipoBienService);
  
  private dialogRef= inject(MatDialogRef);
  public data = inject(MAT_DIALOG_DATA);

  private comunService = inject(ComunService);

  public comunForm!: FormGroup;

  estadoFormulario: string = "";
  responsables: Responsable[]=[];
  tipos: TipoBien[]=[];
  grupos: Grupo[]=[];

  ngOnInit(): void {
    this.getResponsabless();
    this.getCategories();
    this.getTiposs();

    this.estadoFormulario = "Agregar";
    this.comunForm = this.fb.group( {
      responsable: ['', Validators.required],
      tipo: ['', Validators.required],
      grupo: ['', Validators.required],
      descripcomun: ['', Validators.required],
      descripcortacomun: ['', Validators.required]
      //picture: ['', Validators.required]
    })

    if (this.data != null ){
      this.updateForm(this.data);
      this.estadoFormulario = "Actualizar";
    }
  }

  onSave(){
    let data = {
      responsableId: this.comunForm.get('responsable')?.value,
      tipoId: this.comunForm.get('tipo')?.value,
      grupoId: this.comunForm.get('grupo')?.value,
      descripcomun: this.comunForm.get('descripcomun')?.value,
      descripcortacomun: this.comunForm.get('descripcortacomun')?.value,
      ///grupo: this.comunForm.get('grupo')?.value
      ///grupo: this.comunForm.get('grupo')?.value
      //picture: this.selectedFile
    }
/*
    const uploadImageData = new FormData();
    //uploadImageData.append('picture', data.picture, data.picture.name);
    uploadImageData.append('modelo', data.modelo);
    uploadImageData.append('marca', data.marca);
    uploadImageData.append('nroserie', data.nroserie);
    uploadImageData.append('fechaingreso', data.fechaingreso);
    uploadImageData.append('importe', data.importe);
    uploadImageData.append('moneda', data.moneda);
    uploadImageData.append('grupoId', data.grupo);

    if (this.data != null){
      //update the comun
      this.comunService.updateComun(uploadImageData, this.data.id)
                .subscribe( (data: any) =>{
                  this.dialogRef.close(1);
                }, (error: any) => {
                  this.dialogRef.close(2);
                })
    } else {
      //call the service to save a comun
      this.comunService.saveComun(uploadImageData)
              .subscribe( (data: any) =>{
                this.dialogRef.close(1);
              }, (error: any) => {
                this.dialogRef.close(2);
              })
    }*/
    if (this.data != null) {
      // Actualizar el comun
      this.comunService.updateComun(data, this.data.id).subscribe(
        (data: any) => {
          this.dialogRef.close(1);
        },
        (error: any) => {
          this.dialogRef.close(2);
        }
      );
    } else {
      // Guardar un nuevo comun
      this.comunService.saveComun(data).subscribe(
        (data: any) => {
          this.dialogRef.close(1);
        },
        (error: any) => {
          this.dialogRef.close(2);
        }
      );
    }    

  }

  onCancel(){
    this.dialogRef.close(3);
  }

  getResponsabless(){
    this.responsableService.getResponsables()
        .subscribe( (data: any) =>{
          this.responsables = data.responsableResponse.listaresponsables;
        }, (error: any) =>{
          console.log("error al consultar responsables");
        })
  }  

  getCategories(){
    this.grupoService.getGrupos()
        .subscribe( (data: any) =>{
          this.grupos = data.grupoResponse.listagrupos;
        }, (error: any) =>{
          console.log("error al consultar grupos");
        })
  }

  getTiposs(){
    this.tipoService.getTipoBienes()
        .subscribe( (data: any) =>{
          this.tipos = data.tipoResponse.listatipos;
        }, (error: any) =>{
          console.log("error al consultar tipos");
        })
  }    


  /*onFileChanged(event: any){

    this.selectedFile = event.target.files[0];
    console.log(this.selectedFile);

    this.nameImg = event.target.files[0].name;


  }*/

  updateForm(data: any){

    this.comunForm = this.fb.group( {
      responsable: [data.responsable.id, Validators.required],
      tipo: [data.tipo.id, Validators.required],
      grupo: [data.grupo.id, Validators.required],
      descripcomun: [data.descripcomun.id, Validators.required],
      descripcortacomun: [data.descripcortacomun.id, Validators.required]
      //picture: ['', Validators.required]
    })
  }

}

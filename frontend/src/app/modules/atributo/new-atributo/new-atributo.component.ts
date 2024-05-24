import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators, AbstractControl } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ResponsableService } from '../../shared/services/responsable.service';
import { ArticuloService } from '../../shared/services/articulo.service';
import { AtributoService } from '../../shared/services/atributo.service';
import { ChangeDetectorRef } from '@angular/core';

export interface Responsable{
  nombresyapellidos: string;
  id: number;
  arearesponsable: string;
}
export interface Articulo{
  descriparticulo: string;
  id: number;
  nombrearticulo: string;
}
@Component({
  selector: 'app-new-atributo',
  templateUrl: './new-atributo.component.html',
  styleUrls: ['./new-atributo.component.css']
})
export class NewAtributoComponent implements OnInit{
  
  private fb = inject(FormBuilder);
  private responsableService=inject(ResponsableService);
  private articuloService=inject(ArticuloService);
  
  private dialogRef= inject(MatDialogRef);
  public data = inject(MAT_DIALOG_DATA);

  private atributoService = inject(AtributoService);
  private cdr = inject(ChangeDetectorRef)
  public atributoForm!: FormGroup;

  estadoFormulario: string = "";
  responsables: Responsable[]=[];
  articulos: Articulo[]=[];

  ngOnInit(): void {
    this.getResponsabless();
    this.getArticuloss();

    this.estadoFormulario = "Registro";

    this.atributoForm = this.fb.group({
      responsableid: ['', Validators.required],
      articuloid: ['', Validators.required],
      atributos: this.fb.array([
        this.fb.group({
          atributoid: '',
          nombreatributo: '',
          //descripatributo: '',
        }),
      ]),
    });    

    /*this.atributoForm = this.fb.group( {
      responsable: ['', Validators.required],
      articulo:['', Validators.required],
      atributosArray: this.fb.array([])
      /*atributosArray: this.fb.array([
        this.fb.control(null)
      ])    * /  
    })*/

    if (this.data != null ){
      this.updateForm(this.data);
      this.estadoFormulario = "Actualización";
    }  
  }

  get atributosArray(): FormArray {
    return this.atributoForm.get('atributos') as FormArray;
  }
  /*get atributosArray(): FormArray {
    return this.atributoForm.get('atributosArray') as FormArray;
  }*/

  getJsonValuex() {
    const responsableidControl = this.atributoForm.get('responsableid');
    const articuloidControl = this.atributoForm.get('articuloid');
    const atributosControl = this.atributoForm.get('atributos');
  
    if (responsableidControl && articuloidControl && atributosControl) {
      const value = {
        responsableid: responsableidControl.value,
        articuloid: articuloidControl.value,
        atributos: atributosControl.value,
      };
      console.log(JSON.stringify(value, null, 2));
    } else {
      console.error('Error: Al menos uno de los controles es null.');
    }
  }  

  getJsonValue() {
    const responsableidControl = this.atributoForm.get('responsableid');
    const articuloidControl = this.atributoForm.get('articuloid');
    const atributosArray = this.atributoForm.get('atributos') as FormArray;
    
    if (responsableidControl && articuloidControl && atributosArray) {
      const value = {
        responsableid: responsableidControl.value,
        articuloid: articuloidControl.value,
        atributos: atributosArray.value,
      };
      console.log(JSON.stringify(value, null, 2));
    } else {
      console.error('Error: Al menos uno de los controles es null.');
    }
  }
/*
  getPhonesFormControls(): AbstractControl[] {
    return (<FormArray> this.atributoForm.get('atributosArray')).controls
  }

  addAtributo(): void {
    (this.atributoForm.get('atributosArray') as FormArray).push(
      this.fb.control(null)
    );
  }

  removeAtributo(index: number): void {
    (this.atributoForm.get('atributosArray') as FormArray).removeAt(index);
  }  */

  /*addAtributo(): void {
    this.atributosArray.push(this.createAtributo());
  }

  removeAtributo(index: number): void {
    this.atributosArray.removeAt(index);
  }*/

  /*addAtributo(): void {
    const newAtributo = this.fb.group({
        atributo: ['', Validators.required]
    });
    this.atributosArray.push(newAtributo);
  }*/
  addAtributo() {
    this.atributosArray.push(
      this.fb.group({
        atributoid: '',
        nombreatributo: '',
        //descripatributo: '',
      })
    );
    this.cdr.detectChanges();
  }  

  removeAtributo(index: number): void {
    this.atributosArray.removeAt(index);
  }  

  submitAtributoForm(): void {
    if (this.atributoForm.valid) {
      // Enviar formulario al servicio
      const formData = this.atributoForm.value;
      this.atributoService.saveAtributo(formData)
        .subscribe(
          response => {
            console.log('Atributo creado exitosamente:', response);
            // Manejar respuesta exitosa aquí
          },
          error => {
            console.error('Error al crear atributo:', error);
            // Manejar error aquí
          }
        );
    } else {
      // Marcar campos inválidos
      this.markFormGroupTouched(this.atributoForm);
    }
  }

  markFormGroupTouched(formGroup: FormGroup) {
    /*Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();

      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });*/
  }  

  createAtributo(): FormGroup {
    return this.fb.group({
      //atributos: ['', Validators.required]
      atributo: ['', Validators.required]      
    });
  }
  
  onSave(): void {
      let data = {
        responsableId: this.atributoForm.get('responsableid')?.value,
        articuloId: this.atributoForm.get('articuloid')?.value,
        atributos: this.atributoForm.get('atributos')?.value,
      };
      if (this.data != null ){
        //update registry
        this.atributoService.updateAtributo(data, this.data.id)
                .subscribe( (data: any) =>{
                  this.dialogRef.close(1);
                }, (error:any) =>{
                  this.dialogRef.close(2);
                })
      } else {
        //create new registry
        this.atributoService.saveAtributo(data)
            .subscribe( (data : any) => {
              console.log(data);
              this.dialogRef.close(1);
            }, (error: any) => {
              this.dialogRef.close(2);
            })
      }
  }

  
  onSaveXX(): void {
    if (this.atributoForm.valid) {
        let data = {
            responsableId: this.atributoForm.get('responsable')?.value,
            articuloId: this.atributoForm.get('articulo')?.value,
            atributos: this.atributoForm.get('atributosArray')?.value.map((atributo: { atributo: string }) => {
                return { atributo: atributo.atributo };
            })
        };

        this.atributoService.saveAtributo(data)
            .subscribe(
                response => {
                    console.log('Atributo creado exitosamente:', response);
                    // Manejar respuesta exitosa aquí
                },
                error => {
                    console.error('Error al crear atributo:', error);
                    // Manejar error aquí
                }
            );
    } else {
        // Marcar campos inválidos
        this.markFormGroupTouched(this.atributoForm);
    }
}


  /*onSave(): void {
    if (this.atributoForm.valid) {
      let data = {
        responsableId: this.atributoForm.get('responsable')?.value,
        articuloId: this.atributoForm.get('articulo')?.value,
        atributos: this.atributoForm.get('atributosArray')?.value.map((atributo: { descripcion: string }) => {
          return { descripcion: atributo.descripcion };
        })
      };
  
      this.atributoService.saveAtributo(data)
        .subscribe(
          response => {
            console.log('Atributo creado exitosamente:', response);
            // Manejar respuesta exitosa aquí
          },
          error => {
            console.error('Error al crear atributo:', error);
            // Manejar error aquí
          }
        );
    } else {
      // Marcar campos inválidos
      this.markFormGroupTouched(this.atributoForm);
    }
  }*/
  
  updateForm(data: any): void {
    this.atributoForm.patchValue({
      responsable: data.responsable.id,
      articulo: data.articulo.id
    });
    data.atributos.forEach((atributo: any) => {
      this.atributosArray.push(this.fb.group({
        atributo: [atributo.atributo, Validators.required]
      }));
    });
  }
  

  /*onSave(): void {
    if (this.atributoForm.valid) {
      let data = {
        responsableId: this.atributoForm.get('responsableId')?.value,
        articuloId: this.atributoForm.get('articuloId')?.value,
        atributos: this.atributoForm.get('atributos')?.value.map((atributo: { descripcion: string }) => {
          return { descripcion: atributo.descripcion };
        })
      };
  
      this.atributoService.crearAtributo(data)
        .subscribe(
          response => {
            console.log('Atributo creado exitosamente:', response);
            // Manejar respuesta exitosa aquí
          },
          error => {
            console.error('Error al crear atributo:', error);
            // Manejar error aquí
          }
        );
    } else {
      // Marcar campos inválidos
      this.markFormGroupTouched(this.atributoForm);
    }
  }*/
  

  onSaveX(){
    let data = {
      atributos: this.atributoForm.get('atributos')?.value,
      responsableId: this.atributoForm.get('responsable')?.value,
      articuloId: this.atributoForm.get('articulo')?.value
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
      //update the atributo
      this.atributoService.updateAtributo(uploadImageData, this.data.id)
                .subscribe( (data: any) =>{
                  this.dialogRef.close(1);
                }, (error: any) => {
                  this.dialogRef.close(2);
                })
    } else {
      //call the service to save a atributo
      this.atributoService.saveAtributo(uploadImageData)
              .subscribe( (data: any) =>{
                this.dialogRef.close(1);
              }, (error: any) => {
                this.dialogRef.close(2);
              })
    }*/
    if (this.data != null) {
      // Actualizar el atributo
      this.atributoService.updateAtributo(data, this.data.id).subscribe(
        (data: any) => {
          this.dialogRef.close(1);
        },
        (error: any) => {
          this.dialogRef.close(2);
        }
      );
    } else {
      // Guardar un nuevo atributo
      this.atributoService.saveAtributo(data).subscribe(
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

  getArticuloss(){
    this.articuloService.getArticulos()
        .subscribe( (data: any) =>{
          this.articulos = data.articuloResponse.listaarticulos;
        }, (error: any) =>{
          console.log("error al consultar articulos");
        })
  }  

  /*onFileChanged(event: any){

    this.selectedFile = event.target.files[0];
    console.log(this.selectedFile);

    this.nameImg = event.target.files[0].name;


  }*/

  /*updateForm(data: any){

    this.atributoForm = this.fb.group( {
      atributos: [data.atributos, Validators.required],
      responsable: [data.responsable.id, Validators.required],
      articulo: [data.articulo.id, Validators.required]
      //picture: ['', Validators.required]
    })
  }*/

}

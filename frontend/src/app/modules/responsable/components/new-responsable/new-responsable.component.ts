import { Component, Inject, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ResponsableService } from 'src/app/modules/shared/services/responsable.service';

@Component({
  selector: 'app-new-responsable',
  templateUrl: './new-responsable.component.html',
  styleUrls: ['./new-responsable.component.css']
})
export class NewResponsableComponent implements OnInit{

  public responsableForm!: FormGroup;
  private fb = inject(FormBuilder);
  private responsableService= inject(ResponsableService);
  private dialogRef= inject(MatDialogRef);
  public data = inject(MAT_DIALOG_DATA);
  estadoFormulario: string = "";
  idAlfanumerico: string = "";

  ngOnInit(): void {
    this.initializeForm();
    //console.log(this.data);
    //this.estadoFormulario = "Agregar";
    
    /*this.responsableForm = this.fb.group({
      arearesponsable: ['', Validators.required],
      nombresyapellidos: ['', Validators.required]
    })*/

    if (this.data != null ){
      this.updateForm(this.data);
      this.estadoFormulario = "Actualizar";
    } else {
      this.estadoFormulario = "Agregar";
      this.generateNewIdAlfanumerico();
    }
  }

  initializeForm() {
    this.responsableForm = this.fb.group({
      idAlfanumerico: [{ value: '', disabled: true }],
      arearesponsable: ['', Validators.required],
      nombresyapellidos: ['', Validators.required]
    });
  }

  async generateNewIdAlfanumerico() {
    this.responsableService.getResponsables().subscribe((response: any) => {
      if (response.metadata[0].code === "00") {
        const listResponsable = response.responsableResponse.listaresponsables;
        const newId = listResponsable.length + 1;
        this.idAlfanumerico = `RES${newId}`;
        this.responsableForm.get('idAlfanumerico')?.setValue(this.idAlfanumerico);
      } else {
        console.error('Error fetching groups to generate ID');
        this.idAlfanumerico = 'RES1';
        this.responsableForm.get('idAlfanumerico')?.setValue(this.idAlfanumerico);
      }
    }, error => {
      console.error('Error fetching groups to generate ID', error);
      this.idAlfanumerico = 'RES1';
      this.responsableForm.get('idAlfanumerico')?.setValue(this.idAlfanumerico);
    });
  }   

  onSave(){

    let data = {
      arearesponsable: this.responsableForm.get('arearesponsable')?.value,
      nombresyapellidos: this.responsableForm.get('nombresyapellidos')?.value
    }

    if (this.data != null ){
      //update registry
      this.responsableService.updateResponsable(data, this.data.id)
              .subscribe( (data: any) =>{
                this.dialogRef.close(1);
              }, (error:any) =>{
                this.dialogRef.close(2);
              })
    } else {
      //create new registry
      this.responsableService.saveResponsable(data)
          .subscribe( (data : any) => {
            console.log(data);
            this.dialogRef.close(1);
          }, (error: any) => {
            this.dialogRef.close(2);
          })
    }
  }

  onCancel(){
    this.dialogRef.close(3);
  }
/*
  updateForm(data: any){
    this.responsableForm = this.fb.group( {
      arearesponsable: [data.arearesponsable, Validators.required],
      nombresyapellidos: [data.nombresyapellidos, Validators.required]
    });
  }*/
  updateForm(data: any) {
    this.idAlfanumerico = `GRU${data.id}`;
    this.responsableForm.setValue({
      idAlfanumerico: this.idAlfanumerico,
      arearesponsable: data.arearesponsable,
      nombresyapellidos: data.nombresyapellidos
    });
  }

}

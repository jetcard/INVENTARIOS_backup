import { Component, Inject, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DependenciaService } from 'src/app/modules/shared/services/dependencia.service';

@Component({
  selector: 'app-new-dependencia',
  templateUrl: './new-dependencia.component.html',
  styleUrls: ['./new-dependencia.component.css']
})
export class NewDependenciaComponent implements OnInit{

  public dependenciaForm!: FormGroup;
  private fb = inject(FormBuilder);
  private dependenciaService= inject(DependenciaService);
  private dialogRef= inject(MatDialogRef);
  public data = inject(MAT_DIALOG_DATA);
  estadoFormulario: string = "";

  ngOnInit(): void {

    console.log(this.data);
    this.estadoFormulario = "Agregar";
    
    this.dependenciaForm = this.fb.group({
      nombredependencia: ['', Validators.required],
      descripdependencia: ['', Validators.required]
    })

    if (this.data != null ){
      this.updateForm(this.data);
      this.estadoFormulario = "Actualizar";
    }
  }

  onSave(){

    let data = {
      nombredependencia: this.dependenciaForm.get('nombredependencia')?.value,
      descripdependencia: this.dependenciaForm.get('descripdependencia')?.value
    }

    if (this.data != null ){
      //update registry
      this.dependenciaService.updateDependencia(data, this.data.id)
              .subscribe( (data: any) =>{
                this.dialogRef.close(1);
              }, (error:any) =>{
                this.dialogRef.close(2);
              })
    } else {
      //create new registry
      this.dependenciaService.saveDependencia(data)
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

  updateForm(data: any){
    this.dependenciaForm = this.fb.group( {
      nombredependencia: [data.nombredependencia, Validators.required],
      descripdependencia: [data.descripdependencia, Validators.required]
    });

  }


}

import { Component, Inject, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { TipoBienService } from 'src/app/modules/shared/services/tipobien.service';

@Component({
  selector: 'app-new-tipobien',
  templateUrl: './new-tipobien.component.html',
  styleUrls: ['./new-tipobien.component.css']
})
export class NewTipoBienComponent implements OnInit{

  public tipoBienForm!: FormGroup;
  private fb = inject(FormBuilder);
  private tipoBienService= inject(TipoBienService);
  private dialogRef= inject(MatDialogRef);
  public data = inject(MAT_DIALOG_DATA);
  estadoFormulario: string = "";

  ngOnInit(): void {

    console.log(this.data);
    this.estadoFormulario = "Agregar";
    
    this.tipoBienForm = this.fb.group({
      nombretipo: ['', Validators.required],
      descriptipo: ['', Validators.required]
    })

    if (this.data != null ){
      this.updateForm(this.data);
      this.estadoFormulario = "Actualizar";
    }
  }

  onSave(){

    let data = {
      nombretipo: this.tipoBienForm.get('nombretipo')?.value,
      descriptipo: this.tipoBienForm.get('descriptipo')?.value
    }

    if (this.data != null ){
      //update registry
      this.tipoBienService.updateTipoBien(data, this.data.id)
              .subscribe( (data: any) =>{
                this.dialogRef.close(1);
              }, (error:any) =>{
                this.dialogRef.close(2);
              })
    } else {
      //create new registry
      this.tipoBienService.saveTipoBien(data)
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
    this.tipoBienForm = this.fb.group( {
      nombretipo: [data.nombretipo, Validators.required],
      descriptipo: [data.descriptipo, Validators.required]
    });

  }


}

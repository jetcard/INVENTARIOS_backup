import { Component, Inject, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ArticuloService } from 'src/app/modules/shared/services/articulo.service';

@Component({
  selector: 'app-new-articulo',
  templateUrl: './new-articulo.component.html',
  styleUrls: ['./new-articulo.component.css']
})
export class NewArticuloComponent implements OnInit{

  public articuloForm!: FormGroup;
  private fb = inject(FormBuilder);
  private articuloService= inject(ArticuloService);
  private dialogRef= inject(MatDialogRef);
  public data = inject(MAT_DIALOG_DATA);
  estadoFormulario: string = "";

  ngOnInit(): void {

    console.log(this.data);
    this.estadoFormulario = "Agregar";
    
    this.articuloForm = this.fb.group({
      nombrearticulo: ['', Validators.required],
      descriparticulo: ['', Validators.required]
    })

    if (this.data != null ){
      this.updateForm(this.data);
      this.estadoFormulario = "Actualizar";
    }
  }

  onSave(){

    let data = {
      nombrearticulo: this.articuloForm.get('nombrearticulo')?.value,
      descriparticulo: this.articuloForm.get('descriparticulo')?.value
    }

    if (this.data != null ){
      //update registry
      this.articuloService.updateArticulo(data, this.data.id)
              .subscribe( (data: any) =>{
                this.dialogRef.close(1);
              }, (error:any) =>{
                this.dialogRef.close(2);
              })
    } else {
      //create new registry
      this.articuloService.saveArticulo(data)
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
    this.articuloForm = this.fb.group( {
      nombrearticulo: [data.nombrearticulo, Validators.required],
      descriparticulo: [data.descriparticulo, Validators.required]
    });

  }


}

import { Component, Inject, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ProveedorService } from 'src/app/modules/shared/services/proveedor.service';

@Component({
  selector: 'app-new-proveedor',
  templateUrl: './new-proveedor.component.html',
  styleUrls: ['./new-proveedor.component.css']
})
export class NewProveedorComponent implements OnInit{

  public proveedorForm!: FormGroup;
  private fb = inject(FormBuilder);
  private proveedorService= inject(ProveedorService);
  private dialogRef= inject(MatDialogRef);
  public data = inject(MAT_DIALOG_DATA);
  estadoFormulario: string = "";

  ngOnInit(): void {

    console.log(this.data);
    this.estadoFormulario = "Agregar";
    
    this.proveedorForm = this.fb.group({
      nombreproveedor: ['', Validators.required],
      descripproveedor: ['', Validators.required]
    })

    if (this.data != null ){
      this.updateForm(this.data);
      this.estadoFormulario = "Actualizar";
    }
  }

  onSave(){

    let data = {
      nombreproveedor: this.proveedorForm.get('nombreproveedor')?.value,
      descripproveedor: this.proveedorForm.get('descripproveedor')?.value
    }

    if (this.data != null ){
      //update registry
      this.proveedorService.updateProveedor(data, this.data.id)
              .subscribe( (data: any) =>{
                this.dialogRef.close(1);
              }, (error:any) =>{
                this.dialogRef.close(2);
              })
    } else {
      //create new registry
      this.proveedorService.saveProveedor(data)
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
    this.proveedorForm = this.fb.group( {
      nombreproveedor: [data.nombreproveedor, Validators.required],
      descripproveedor: [data.descripproveedor, Validators.required]
    });

  }


}

import { Component, inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { GrupoService } from '../../services/grupo.service';
import { ActivoService } from '../../services/activo.service';

@Component({
  selector: 'app-confirm',
  templateUrl: './confirm.component.html',
  styleUrls: ['./confirm.component.css']
})
export class ConfirmComponent {

  private GrupoService= inject(GrupoService);
  private dialogRef= inject(MatDialogRef);
  public data = inject(MAT_DIALOG_DATA);
  private activoService= inject(ActivoService);


  onNoClick(){
    this.dialogRef.close(3)
  }

  delete(){
    if (this.data != null){

      if (this.data.module == "grupo") {
      
        this.GrupoService.deleteGrupo(this.data.id).
              subscribe( (data:any) =>{
                this.dialogRef.close(1);
              }, (error: any) => {
                this.dialogRef.close(2);
              })
      } else if ( this.data.module == "activo" )  {
            this.activoService.deleteActivo(this.data.id).
              subscribe( (data:any) =>{
                this.dialogRef.close(1);
              }, (error: any) => {
                this.dialogRef.close(2);
              })
      } 

    } else {
      this.dialogRef.close(2);
    }
  }

}

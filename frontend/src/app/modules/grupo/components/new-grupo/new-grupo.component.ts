import { Component, Inject, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { GrupoService } from 'src/app/modules/shared/services/grupo.service';

@Component({
  selector: 'app-new-grupo',
  templateUrl: './new-grupo.component.html',
  styleUrls: ['./new-grupo.component.css']
})
export class NewGrupoComponent implements OnInit{

  public grupoForm!: FormGroup;
  private fb = inject(FormBuilder);
  private grupoService= inject(GrupoService);
  private dialogRef= inject(MatDialogRef);
  public data = inject(MAT_DIALOG_DATA);
  estadoFormulario: string = "";
  idAlfanumerico: string = "";

  ngOnInit(): void {
    this.initializeForm();

    if (this.data != null) {
      this.updateForm(this.data);
      this.estadoFormulario = "Actualizar";
    } else {
      this.estadoFormulario = "Agregar";
      // Generate the alphanumeric ID for new records
      this.generateNewIdAlfanumerico();
    }

    /*console.log(this.data);
    this.estadoFormulario = "Agregar";
    
    this.grupoForm = this.fb.group({
      idAlfanumerico: [{value: '', disabled: true}],
      nombregrupo: ['', Validators.required],
      descripgrupo: ['', Validators.required]
    })

    if (this.data != null ){
      this.updateForm(this.data);
      this.estadoFormulario = "Actualizar";
    } else {
      // Generate the alphanumeric ID for new records
      this.grupoService.getGrupos().subscribe((response: any) => {
        const newId = response.length + 1;
        this.idAlfanumerico = `GRU${newId}`;
        this.grupoForm.get('idAlfanumerico')?.setValue(this.idAlfanumerico);
      });
    }*/
  }

  initializeForm() {
    this.grupoForm = this.fb.group({
      idAlfanumerico: [{ value: '', disabled: true }],
      nombregrupo: ['', Validators.required],
      descripgrupo: ['', Validators.required]
    });
  }

  /*async generateNewIdAlfanumerico() {
    this.grupoService.getGrupos().subscribe((response: any) => {
      const newId = response.length + 1;
      this.idAlfanumerico = `GRU${newId}`;
      this.grupoForm.get('idAlfanumerico')?.setValue(this.idAlfanumerico);
    });
  }*/

  async generateNewIdAlfanumerico() {
    this.grupoService.getGrupos().subscribe((response: any) => {
      if (response.metadata[0].code === "00") {
        const listGrupo = response.grupoResponse.listagrupos;
        const newId = listGrupo.length + 1;
        this.idAlfanumerico = `GRU${newId}`;
        this.grupoForm.get('idAlfanumerico')?.setValue(this.idAlfanumerico);
      } else {
        console.error('Error fetching groups to generate ID');
        this.idAlfanumerico = 'GRU1';
        this.grupoForm.get('idAlfanumerico')?.setValue(this.idAlfanumerico);
      }
    }, error => {
      console.error('Error fetching groups to generate ID', error);
      this.idAlfanumerico = 'GRU1';
      this.grupoForm.get('idAlfanumerico')?.setValue(this.idAlfanumerico);
    });
  }  

  onSave(){

    let data = {
      nombregrupo: this.grupoForm.get('nombregrupo')?.value,
      descripgrupo: this.grupoForm.get('descripgrupo')?.value
    }

    if (this.data != null ){
      //update registry
      this.grupoService.updateGrupo(data, this.data.id)
              .subscribe( (data: any) =>{
                this.dialogRef.close(1);
              }, (error:any) =>{
                this.dialogRef.close(2);
              })
    } else {
      //create new registry
      this.grupoService.saveGrupo(data)
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

  /*updateForm(data: any){
    this.idAlfanumerico = `GRU${data.id}`;
    this.grupoForm = this.fb.group( {
      idAlfanumerico: [{value: this.idAlfanumerico, disabled: true}],
      nombregrupo: [data.nombregrupo, Validators.required],
      descripgrupo: [data.descripgrupo, Validators.required]
    });

  }*/
  updateForm(data: any) {
    this.idAlfanumerico = `GRU${data.id}`;
    this.grupoForm.setValue({
      idAlfanumerico: this.idAlfanumerico,
      nombregrupo: data.nombregrupo,
      descripgrupo: data.descripgrupo
    });
  }

}

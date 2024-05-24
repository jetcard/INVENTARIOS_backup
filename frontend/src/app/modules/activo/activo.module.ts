import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../shared/material.module';
import { ActivoComponent } from './activo/activo.component';
import { NewActivoComponent } from './new-activo/new-activo.component';
//import { DatePipe } from '@angular/common';

@NgModule({
  declarations: [
    ActivoComponent,
    NewActivoComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule
  ],
 /*providers: [
    DatePipe, // Agrega DatePipe a la lista de proveedores
    ///ActivoService // Si no lo has hecho ya
  ]*/
})
export class ActivoModule { }

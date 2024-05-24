import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../shared/material.module';
import { ComunComponent } from './comun/comun.component';
import { NewComunComponent } from './new-comun/new-comun.component';

@NgModule({
  declarations: [
    ComunComponent,
    NewComunComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class ComunModule { }

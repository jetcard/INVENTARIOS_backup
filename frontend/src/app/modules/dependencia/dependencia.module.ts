import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DependenciaComponent } from './components/dependencia/dependencia.component';
import { MaterialModule } from '../shared/material.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NewDependenciaComponent } from './components/new-dependencia/new-dependencia.component';

@NgModule({
  declarations: [
    DependenciaComponent,
    NewDependenciaComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class DependenciaModule { }
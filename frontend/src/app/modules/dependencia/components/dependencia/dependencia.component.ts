import { Component, Inject, inject, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar, MatSnackBarRef, SimpleSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { ConfirmComponent } from 'src/app/modules/shared/components/confirm/confirm.component';
import { DependenciaService } from 'src/app/modules/shared/services/dependencia.service';
import { UtilService } from 'src/app/modules/shared/services/util.service';
import { NewDependenciaComponent } from '../new-dependencia/new-dependencia.component';
import { Router } from '@angular/router'; // Importa Router

import { MatDialogModule } from '@angular/material/dialog';
import { ResponsableComponent } from './../../../responsable/components/responsable/responsable.component';
import { TipoBienComponent } from './../../../tipobien/components/tipobien/tipobien.component';
import { GrupoComponent } from './../../../grupo/components/grupo/grupo.component';
import { ArticuloComponent } from './../../../articulo/components/articulo/articulo.component';
import { ActivoComponent } from './../../../activo/activo/activo.component';
import { ProveedorComponent } from './../../../proveedor/components/proveedor/proveedor.component';
import { ComunComponent } from './../../../comun/comun/comun.component';
import { AtributoComponent } from './../../../atributo/atributo/atributo.component';
import { AtributosComponent } from './../../../atributos/atributos/atributos.component';


@Component({
  selector: 'app-dependencia',
  templateUrl: './dependencia.component.html',
  styleUrls: ['./dependencia.component.css']
})
export class DependenciaComponent implements OnInit{

  isAdmin: any;
  private router = inject(Router);
  private dependenciaService = inject(DependenciaService);
  private snackBar = inject(MatSnackBar);
  public dialog = inject(MatDialog);
  private util = inject (UtilService);

  menuNav = [
    { name: "Responsable", route: "responsable", icon: "moneda_box" },
    { name: "Tipo de Bien", route: "tipobien", icon: "desktop" },
    { name: "Articulos", route: "articulo", icon: "assessments" },
    { name: "Grupos", route: "grupo", icon: "tablet" },
//    { name: "Activos", route: "activo", icon: "card_travel" },
    { name: "Proveedores", route: "proveedor", icon: "assignment" },
//    { name: "Dependencias", route: "dependencia", icon: "assignment" },
    { name: "Comunes", route: "comun", icon: "wallet" },
    { name: "Atributo", route: "atributo", icon: "bookmark" },
    { name: "Atributos", route: "atributos", icon: "bookmark" }
  ];

  selectedComponent: any;

  ngOnInit(): void {
    console.log(this.util.getRoles());
    this.isAdmin = this.util.isAdmin();
  }

  dataSource = new MatTableDataSource<DependenciaElement>();

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  processDependenciasResponse(resp: any){

    const dataDependencia: DependenciaElement[] = [];

    if( resp.metadata[0].code == "00") {

      let listDependencia = resp.dependenciaResponse.listadependencias;

      listDependencia.forEach((element: DependenciaElement) => {
        dataDependencia.push(element);
      });

      this.dataSource = new MatTableDataSource<DependenciaElement>(dataDependencia);
      this.dataSource.paginator = this.paginator;
      
    }

  }

  
  navigateTo(route: string) {
    //this.router.navigate([route]);
    this.selectedComponent = this.getComponentByRoute(route);
  }

  getComponentByRoute(route: string) {
    switch(route) {
      case 'responsable': return ResponsableComponent;
      case 'tipobien': return TipoBienComponent;
      case 'articulo': return ArticuloComponent;
      case 'grupo': return GrupoComponent;
      case 'activo': return ActivoComponent;
      case 'proveedor': return ProveedorComponent;
      case 'comun': return ComunComponent;
      case 'atributo': return AtributoComponent;
      case 'atributos': return AtributosComponent;
      default: return null;
    }
  }


}

export interface DependenciaElement {
  descripdependencia: string;
  id: number;
  nombredependencia: string;
  
}

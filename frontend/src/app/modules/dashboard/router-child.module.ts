import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ResponsableComponent } from '../responsable/components/responsable/responsable.component';
import { TipoBienComponent } from '../tipobien/components/tipobien/tipobien.component';
import { GrupoComponent } from '../grupo/components/grupo/grupo.component';
import { ArticuloComponent } from '../articulo/components/articulo/articulo.component';
import { ActivoComponent } from '../activo/activo/activo.component';
import { ProveedorComponent } from '../proveedor/components/proveedor/proveedor.component';
import { HomeComponent } from './components/home/home.component';
import { DependenciaComponent } from '../dependencia/components/dependencia/dependencia.component';
import { ComunComponent } from '../comun/comun/comun.component';
import { AtributoComponent } from '../atributo/atributo/atributo.component';
import { AtributosComponent } from '../atributos/atributos/atributos.component';

const childRoutes: Routes = [
    { path: '', component: HomeComponent },
    { path: 'home', component: HomeComponent },
    { path: 'responsable', component: ResponsableComponent },
    { path: 'tipobien', component: TipoBienComponent },
    { path: 'grupo', component: GrupoComponent },
    { path: 'articulo', component: ArticuloComponent },
    { path: 'activo', component: ActivoComponent },
    { path: 'proveedor', component: ProveedorComponent },
    { path: 'dependencia', component: DependenciaComponent },
    { path: 'comun', component: ComunComponent },
    { path: 'atributo', component: AtributoComponent },
    { path: 'atributos', component: AtributosComponent }
]

@NgModule({
    imports: [RouterModule.forChild(childRoutes)],
    exports: [RouterModule]
})
export class RouterChildModule { }
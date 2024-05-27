import { MediaMatcher } from '@angular/cdk/layout';
import { Component, inject } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.css']
})
export class SidenavComponent {

  mobileQuery: MediaQueryList;
  private keycloakService = inject(KeycloakService);

  menuNav = [
    {name: "Home", route: "home", icon: "home"},
    {name: "Activos", route: "activo", icon: "card_travel"},
    {name: "Maestro de tablas", route: "dependencia", icon: "moneda_box"},
  /*  {name: "Responsable", route: "responsable", icon: "moneda_box"},
    {name: "Tipo de Bien", route: "tipobien", icon: "desktop"},
    {name: "Articulos", route: "articulo", icon: "assessments"},
    {name: "Grupos", route: "grupo", icon: "tablet"},
    {name: "Activos", route: "activo", icon: "card_travel"},
    {name: "Proveedores", route: "proveedor", icon: "assignment"},
    {name: "Dependencias", route: "dependencia", icon: "assignment"},
    {name: "Comunes", route: "comun", icon: "wallet"},
    {name: "Atributo", route: "atributo", icon: "bookmark"},
    {name: "Atributos", route: "atributos", icon: "bookmark"} */   
  ]

  constructor(media: MediaMatcher) {
    this.mobileQuery = media.matchMedia('(max-width: 600px)');
  }

  logout(){
    this.keycloakService.logout();

  }

}

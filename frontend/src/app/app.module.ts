import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DashboardModule } from './modules/dashboard/dashboard.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { KeycloakAngularModule , KeycloakService} from 'keycloak-angular';
import { NgChartsModule } from 'ng2-charts';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatGridListModule } from '@angular/material/grid-list';
import { DatePipe } from '@angular/common';
import { MatDatepicker } from '@angular/material/datepicker';
import { MatDatepickerModule } from '@angular/material/datepicker'; // Importa MatDatepickerModule
import { NativeDateAdapter, DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core'; // Importa DateAdapter
import { formatDate } from '@angular/common';

function initializeKeycloak(keycloak: KeycloakService) {
  return () =>
    keycloak.init({
      config: {
        url: 'https://examensolucion-u8698.vm.elestio.app/',
        realm: 'Inventario',
        clientId: 'inventario'
      },
      /*
      config: {
        url: 'https://segurfce.popular-safi.com/',
        realm: 'Inventarios',
        clientId: 'inventario'
      },      
      */
      initOptions: {
        onLoad: 'login-required',
        flow: "standard",
        silentCheckSsoRedirectUri:
          window.location.origin + '/assets/silent-check-sso.html'
      },
      loadUserProfileAtStartUp: true
    });
}

export class AppDateAdapter extends NativeDateAdapter {
  // Sobreescribimos el método format para definir el formato de fecha deseado
  override format(date: Date, displayFormat: Object): string {
    if (displayFormat === 'input') {
      return formatDate(date, 'dd/MM/yyyy', this.locale);
    } else {
      return date.toDateString();
    }
  }
}

export const APP_DATE_FORMATS = {
  parse: {
    dateInput: { month: 'short', year: 'numeric', day: 'numeric' },
  },
  display: {
    dateInput: 'input',
    monthYearLabel: { year: 'numeric', month: 'short' },
    dateA11yLabel: { year: 'numeric', month: 'long', day: 'numeric' },
    monthYearA11yLabel: { year: 'numeric', month: 'long' },
  },
};

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    DashboardModule,
    BrowserAnimationsModule,
    KeycloakAngularModule,
    NgChartsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatGridListModule,    
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [KeycloakService]
    },
    {
      provide: MAT_DATE_FORMATS,
      useValue: APP_DATE_FORMATS,
    },
    {
      provide: DateAdapter,
      useClass: AppDateAdapter,
    },    
    DatePipe
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

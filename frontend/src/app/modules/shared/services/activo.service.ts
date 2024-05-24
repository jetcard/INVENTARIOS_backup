import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DatePipe } from '@angular/common';

const base_url = "https://j7ejwpc0ba.execute-api.ap-southeast-2.amazonaws.com/prod";

@Injectable({
  providedIn: 'root'
})
export class ActivoService {

  constructor(private http: HttpClient, private datePipe: DatePipe) { }
  
  /**
   * get all the activos
   */
  getActivos(){
    const endpoint = `${ base_url}/activos`;
    return this.http.get(endpoint);
  }

  /**
   * save the activo
   */
  saveActivo(body: any){
    const endpoint = `${ base_url}/activos`;
    const fechaingreso = this.datePipe.transform(body.fechaingreso, 'yyyy-MM-dd');
    body.fechaingreso = fechaingreso;    
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    };    
    //return this.http.post(endpoint, body, httpOptions);
    return this.http.post(endpoint, JSON.stringify(body), httpOptions);
  }

  /**
   * update activo
   */
  updateActivo (body: any, id: any){
    const endpoint = `${ base_url}/activos/${id}`;
    return this.http.put(endpoint, body);
  }

  /**
   * delete activo
   */
  deleteActivo(id: any){
    const endpoint = `${ base_url}/activos/${id}`;
    return this.http.delete(endpoint);
  }

  /*getActivoByModelo(modelo: any){
    const endpoint = `${ base_url}/activos/modelo/${modelo}`;
    return this.http.get(endpoint);
  }

  getActivoByMarca(marca: any){
    const endpoint = `${ base_url}/activos/marca/${marca}`;
    return this.http.get(endpoint);
  }

  getActivoBynroserie(nroserie: any){
    const endpoint = `${ base_url}/activos/nroserie/${nroserie}`;
    return this.http.get(endpoint);
  }

  getActivoByFechaingreso(fechaingreso: any){
    const endpoint = `${ base_url}/activos/fechaingreso/${fechaingreso}`;
    return this.http.get(endpoint);
  }*/

  getActivoBusqueda(codinventario: string, modelo: string, marca: string, nroserie: string, fechaingresoDesde: Date | null, fechaingresoHasta: Date | null): Observable<any> {
    let params = new HttpParams();
    if (codinventario) {
      params = params.set('codinventario', codinventario);
    }    
    if (modelo) {
      params = params.set('modelo', modelo);
    }
    if (marca) {
      params = params.set('marca', marca);
    }
    if (nroserie) {
      params = params.set('nroserie', nroserie);
    }
    const datePipe = new DatePipe('en-US');
    if (fechaingresoDesde) {
      const fechaDesdeStr = datePipe.transform(fechaingresoDesde, 'dd-MM-yyyy');
      params = params.set('fechadesde', fechaDesdeStr || '');
    }
    if (fechaingresoHasta) {
      const fechaHastaStr = datePipe.transform(fechaingresoHasta, 'dd-MM-yyyy');
      params = params.set('fechahasta', fechaHastaStr || '');
    }
    return this.http.get(`${base_url}/activos/campos`, { params });
  }
  /**
   * export excel activos
   */
  exportActivo(){
    const endpoint = `${base_url}/activos/export/excel`;
    return this.http.get(endpoint, {
      responseType: 'blob'
    });
  }
}

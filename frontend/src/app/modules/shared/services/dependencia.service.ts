import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

const base_url = "https://j7ejwpc0ba.execute-api.ap-southeast-2.amazonaws.com/prod";

@Injectable({
  providedIn: 'root'
})
export class DependenciaService {

  constructor(private http: HttpClient) { }
  
  /**
   * get all the responsables
   */
  getDependencias(){
    const endpoint = `${ base_url}/dependencias`;
    return this.http.get(endpoint);
  }

  /**
   * save the responsable
   */
  saveDependencia(body: any){
    const endpoint = `${ base_url}/dependencias`;
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    };    
    //return this.http.post(endpoint, body, httpOptions);
    return this.http.post(endpoint, JSON.stringify(body), httpOptions);
  }

  /**
   * update responsable
   */
  updateDependencia(body: any, id: any){
    const endpoint = `${ base_url}/dependencias/${id}`;
    return this.http.put(endpoint, body);
  }

  /**
   * delete responsable
   */
  deleteDependencia(id: any){
    const endpoint = `${ base_url}/dependencias/${id}`;
    return this.http.delete(endpoint);
  }

  /**
   * search by modelo
   */
  getDependenciaByModelo(modelo: any){
    const endpoint = `${ base_url}/dependencias/filter/${modelo}`;
    return this.http.get(endpoint);
  }


  /**
   * export excel responsables
   */
  exportDependencia(){
    const endpoint = `${base_url}/dependencias/export/excel`;
    return this.http.get(endpoint, {
      responseType: 'blob'
    });
  }
}

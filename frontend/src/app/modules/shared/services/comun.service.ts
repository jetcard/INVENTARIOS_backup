import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

const base_url = "https://dj2y8oc2v5.execute-api.ap-southeast-2.amazonaws.com/prod";

@Injectable({
  providedIn: 'root'
})
export class ComunService {

  constructor(private http: HttpClient) { }
  
  /**
   * get all the comuns
   */
  getComunes(){
    const endpoint = `${ base_url}/comunes`;
    return this.http.get(endpoint);
  }

  /**
   * save the comun
   */
  saveComun(body: any){
    const endpoint = `${ base_url}/comunes`;
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    };    
    //return this.http.post(endpoint, body, httpOptions);
    return this.http.post(endpoint, JSON.stringify(body), httpOptions);
  }

  /**
   * update comun
   */
  updateComun (body: any, id: any){
    const endpoint = `${ base_url}/comunes/${id}`;
    return this.http.put(endpoint, body);
  }

  /**
   * delete comun
   */
  deleteComun(id: any){
    const endpoint = `${ base_url}/comunes/${id}`;
    return this.http.delete(endpoint);
  }

  /**
   * search by modelo
   */
  getComunByModelo(modelo: any){
    const endpoint = `${ base_url}/comunes/filter/${modelo}`;
    return this.http.get(endpoint);
  }


  /**
   * export excel comuns
   */
  exportComun(){
    const endpoint = `${base_url}/comunes/export/excel`;
    return this.http.get(endpoint, {
      responseType: 'blob'
    });
  }
}

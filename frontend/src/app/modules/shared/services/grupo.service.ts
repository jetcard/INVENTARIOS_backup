import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

const base_url = "https://j7ejwpc0ba.execute-api.ap-southeast-2.amazonaws.com/prod";

@Injectable({
  providedIn: 'root'
})
export class GrupoService {

  constructor(private http: HttpClient) { }

  getGrupos(){
    const endpoint = `${base_url}/grupos`;
    return this.http.get(endpoint);
  }

  saveGrupo(body: any) {
    const endpoint = `${base_url}/grupos`;
    return this.http.post(endpoint, body);
  }

  updateGrupo(body: any, id: any){
    const endpoint = `${base_url}/grupos/${id}`;
    return this.http.put(endpoint, body);
  }

  deleteGrupo(id: any){
    const endpoint = `${base_url}/grupos/${id}`;
    return this.http.delete(endpoint);
  }

  getGrupoById(id: any){
    const endpoint = `${base_url}/grupos/${id}`;
    return this.http.get(endpoint);
  }


  /**
   * export excel categories
   */
  exportGrupos(){
    const endpoint = `${base_url}/grupos/export/excel`;
    return this.http.get(endpoint, {
      responseType: 'blob'
    });
  }
}

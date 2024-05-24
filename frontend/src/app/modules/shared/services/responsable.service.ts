import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

const base_url = "https://j7ejwpc0ba.execute-api.ap-southeast-2.amazonaws.com/prod";

@Injectable({
  providedIn: 'root'
})
export class ResponsableService {

  constructor(private http: HttpClient) { }

  /**
   * get all responsables
   */
  getResponsables(){
    const endpoint = `${base_url}/responsables`;
    return this.http.get(endpoint);
  }

  /**
   * save the responsables
   */
  saveResponsable(body: any) {
    const endpoint = `${base_url}/responsables`;
    return this.http.post(endpoint, body);
  }

  /**
   * update responsable
   */
  updateResponsable(body: any, id: any){
    const endpoint = `${base_url}/responsables/${id}`;
    return this.http.put(endpoint, body);
  }

  /**
   * update responsables
   */
  deleteResponsable(id: any){
    const endpoint = `${base_url}/responsables/${id}`;
    return this.http.delete(endpoint);
  }

  /**
   * update responsable
   */
  getResponsableById(id: any){
    const endpoint = `${base_url}/responsables/${id}`;
    return this.http.get(endpoint);
  }


  /**
   * export excel responsables
   */
  exportResponsables(){
    const endpoint = `${base_url}/responsables/export/excel`;
    return this.http.get(endpoint, {
      responseType: 'blob'
    });
  }
}

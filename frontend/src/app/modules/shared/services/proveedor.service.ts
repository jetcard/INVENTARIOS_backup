import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

const base_url = "https://dj2y8oc2v5.execute-api.ap-southeast-2.amazonaws.com/prod";

@Injectable({
  providedIn: 'root'
})
export class ProveedorService {

  constructor(private http: HttpClient) { }

  getProveedores(){

    const endpoint = `${base_url}/proveedores`;
    return this.http.get(endpoint);

  }


  saveProveedor(body: any) {
    const endpoint = `${base_url}/proveedores`;
    return this.http.post(endpoint, body);
  }

  /**
   * update categorie
   */
  updateProveedor(body: any, id: any){
    const endpoint = `${base_url}/proveedores/${id}`;
    return this.http.put(endpoint, body);
  }

  /**
   * update categorie
   */
  deleteProveedor(id: any){
    const endpoint = `${base_url}/proveedores/${id}`;
    return this.http.delete(endpoint);
  }

  /**
   * update proveedor
   */
  getProveedorById(id: any){
    const endpoint = `${base_url}/proveedores/${id}`;
    return this.http.get(endpoint);
  }


  /**
   * export excel proveedores
   */
  exportProveedores(){
    const endpoint = `${base_url}/proveedores/export/excel`;
    return this.http.get(endpoint, {
      responseType: 'blob'
    });
  }
}

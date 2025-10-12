import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { map } from 'rxjs/operators';

interface LoginResponse {
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private tokenKey = 'jwtToken';
  private loggedIn = new BehaviorSubject<boolean>(this.hasToken());

  constructor(private http: HttpClient) {}

  private hasToken(): boolean {
    return typeof localStorage !== 'undefined' && !!localStorage.getItem(this.tokenKey);
  }

  login(username: string, password: string): Observable<boolean> {
    return this.http.post<LoginResponse>('/auth/login', { username, password }).pipe(
      map((response: LoginResponse) => {
        if (response && response.token) {
          if (typeof localStorage !== 'undefined') {
            localStorage.setItem(this.tokenKey, response.token);
          }
          this.loggedIn.next(true);
          return true;
        }
        return false;
      })
    );
  }

  logout(): void {
    const token = this.getToken();
    if (token) {
      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
      this.http.post('/auth/logout', {}, { headers }).subscribe(() => {
        if (typeof localStorage !== 'undefined') {
          localStorage.removeItem(this.tokenKey);
        }
        this.loggedIn.next(false);
      });
    } else {
      this.loggedIn.next(false);
    }
  }

  getToken(): string | null {
    if (typeof localStorage === 'undefined') {
      return null;
    }
    return localStorage.getItem(this.tokenKey);
  }

  isLoggedIn(): Observable<boolean> {
    return this.loggedIn.asObservable();
  }

  getUserRole(): string | null {
    const token = this.getToken();
    if (!token) return null;
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.role || null;
    } catch {
      return null;
    }
  }
}
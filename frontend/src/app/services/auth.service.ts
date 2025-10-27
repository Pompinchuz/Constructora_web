import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap, map } from 'rxjs/operators';
import { environment } from '../../environments/environment';

type LoginDto = { email: string; password: string; };
type RegisterDto = { nombre: string; email: string; password: string; };
type JwtResponse = { token: string; user?: any; };

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly api = environment.apiUrl + '/auth';
  // estado simple con signals
  isAuth = signal<boolean>(!!localStorage.getItem('token'));
  user = signal<any | null>(JSON.parse(localStorage.getItem('user') || 'null'));

  constructor(private http: HttpClient) {}

  login(data: LoginDto) {
    return this.http.post<JwtResponse>(`${this.api}/login`, data).pipe(
      tap(res => this.persist(res))
    );
  }

  register(data: RegisterDto) {
    return this.http.post<JwtResponse>(`${this.api}/register`, data).pipe(
      tap(res => this.persist(res))
    );
  }

  me() {
    return this.http.get<any>(`${this.api}/me`).pipe(
      tap(u => this.user.set(u))
    );
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.isAuth.set(false);
    this.user.set(null);
  }

  private persist(res: JwtResponse) {
    localStorage.setItem('token', res.token);
    if (res.user) localStorage.setItem('user', JSON.stringify(res.user));
    this.isAuth.set(true);
    this.user.set(res.user ?? null);
  }

  get token(): string | null {
    return localStorage.getItem('token');
  }
}

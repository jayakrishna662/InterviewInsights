import React, { createContext, useContext, useState, useEffect } from 'react';
import { api } from '../api/client';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem('token') || null);
  const [role, setRole] = useState(() => localStorage.getItem('role') || null);
  const [user, setUser] = useState(() => {
    const saved = localStorage.getItem('user');
    try {
      return saved ? JSON.parse(saved) : null;
    } catch {
      return null;
    }
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function verifyAuth() {
      if (!token) {
        setLoading(false);
        return;
      }
      try {
        const currentUser = await api.get('/auth/me');
        setUser(currentUser);
        setRole(currentUser.role);
        localStorage.setItem('user', JSON.stringify(currentUser));
        localStorage.setItem('role', currentUser.role);
      } catch (err) {
        console.error('Session expired or invalid token:', err);
        logout();
      } finally {
        setLoading(false);
      }
    }
    verifyAuth();
  }, [token]);

  const login = (authData) => {
    // authData from backend AuthResponse: { userId, name, email, token, role, success, message }
    setToken(authData.token);
    setRole(authData.role);
    setUser({
      id: authData.userId,
      name: authData.name,
      email: authData.email,
      role: authData.role,
    });
    localStorage.setItem('token', authData.token);
    localStorage.setItem('role', authData.role);
    localStorage.setItem(
      'user',
      JSON.stringify({
        id: authData.userId,
        name: authData.name,
        email: authData.email,
        role: authData.role,
      })
    );
  };

  const logout = () => {
    setToken(null);
    setRole(null);
    setUser(null);
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('user');
  };

  const isAdmin = role === 'ADMIN';

  return (
    <AuthContext.Provider
      value={{
        token,
        role,
        user,
        loading,
        login,
        logout,
        isAuthenticated: !!token,
        isAdmin,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}

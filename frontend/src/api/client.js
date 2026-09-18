// Lightweight API client with automatic JWT token attachment and error handling

const API_BASE = import.meta.env.VITE_API_BASE_URL || '/api';

export async function apiRequest(endpoint, options = {}) {
  const token = localStorage.getItem('token');
  
  const headers = {
    'Content-Type': 'application/json',
    ...(options.headers || {}),
  };

  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  const config = {
    ...options,
    headers,
  };

  if (options.body && typeof options.body === 'object' && !(options.body instanceof FormData)) {
    config.body = JSON.stringify(options.body);
  }

  const response = await fetch(`${API_BASE}${endpoint}`, config);

  if (response.status === 401) {
    // If unauthorized, clear stale credentials
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('role');
  }

  let data;
  const contentType = response.headers.get('content-type');
  if (contentType && contentType.includes('application/json')) {
    data = await response.json();
  } else {
    data = await response.text();
  }

  if (!response.ok) {
    const errorMessage = (typeof data === 'object' && data?.message) ? data.message : (data || 'Request failed');
    throw new Error(errorMessage);
  }

  return data;
}

export const api = {
  get: (endpoint) => apiRequest(endpoint, { method: 'GET' }),
  post: (endpoint, body) => apiRequest(endpoint, { method: 'POST', body }),
  put: (endpoint, body) => apiRequest(endpoint, { method: 'PUT', body }),
  patch: (endpoint, body) => apiRequest(endpoint, { method: 'PATCH', body }),
  delete: (endpoint) => apiRequest(endpoint, { method: 'DELETE' }),
};

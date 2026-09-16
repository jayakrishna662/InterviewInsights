import React, { useState, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { api } from '../api/client';
import { LogIn, UserPlus, AlertCircle, CheckCircle2, Eye, EyeOff } from 'lucide-react';

export default function Auth() {
  const [searchParams] = useSearchParams();
  const initialTab = searchParams.get('tab') === 'register' ? 'register' : 'login';
  const [tab, setTab] = useState(initialTab);

  const { login, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  // Form states
  const [loginData, setLoginData] = useState({ rollNumber: '', password: '' });
  const [registerData, setRegisterData] = useState({
    name: '',
    email: '',
    rollNumber: '',
    departmentId: '',
    batchId: '',
    password: '',
    confirmPassword: '',
  });

  const [departments, setDepartments] = useState([]);
  const [batches, setBatches] = useState([]);
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');
  const [successMsg, setSuccessMsg] = useState('');
  const [loading, setLoading] = useState(false);

  // Redirect if already logged in
  useEffect(() => {
    if (isAuthenticated) {
      navigate('/');
    }
  }, [isAuthenticated, navigate]);

  // Fetch departments and batches for registration dropdowns
  useEffect(() => {
    async function loadMetadata() {
      try {
        const [deptRes, batchRes] = await Promise.all([
          api.get('/departments'),
          api.get('/batches'),
        ]);
        setDepartments(deptRes || []);
        setBatches(batchRes || []);
      } catch (err) {
        console.error('Failed to load departments/batches:', err);
      }
    }
    loadMetadata();
  }, []);

  const handleLoginSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const response = await api.post('/auth/login', {
        rollNumber: loginData.rollNumber.trim(),
        password: loginData.password,
      });

      if (response && response.token) {
        login(response);
        if (response.role === 'ADMIN') {
          navigate('/admin');
        } else {
          navigate('/');
        }
      } else {
        setError(response?.message || 'Login failed. Please verify credentials.');
      }
    } catch (err) {
      setError(err.message || 'Login failed');
    } finally {
      setLoading(false);
    }
  };

  const handleRegisterSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccessMsg('');

    if (registerData.password !== registerData.confirmPassword) {
      setError('Passwords do not match');
      return;
    }

    setLoading(true);
    try {
      const response = await api.post('/auth/register', {
        name: registerData.name.trim(),
        email: registerData.email.trim(),
        rollNumber: registerData.rollNumber.trim(),
        departmentId: registerData.departmentId,
        batchId: parseInt(registerData.batchId, 10),
        password: registerData.password,
        confirmPassword: registerData.confirmPassword,
      });

      setSuccessMsg('Registration successful! Please log in.');
      setRegisterData({
        name: '',
        email: '',
        rollNumber: '',
        departmentId: '',
        batchId: '',
        password: '',
        confirmPassword: '',
      });
      setTab('login');
    } catch (err) {
      setError(err.message || 'Registration failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{
      flex: 1,
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      padding: '3rem 1.5rem',
      backgroundColor: 'var(--bg-main)',
    }}>
      <div className="card" style={{ width: '100%', maxWidth: '460px', padding: '2.5rem' }}>
        {/* Header Tabs */}
        <div style={{
          display: 'flex',
          backgroundColor: 'var(--bg-main)',
          padding: '0.25rem',
          borderRadius: '8px',
          marginBottom: '2rem',
        }}>
          <button
            type="button"
            onClick={() => { setTab('login'); setError(''); setSuccessMsg(''); }}
            style={{
              flex: 1,
              padding: '0.6rem',
              borderRadius: '6px',
              fontWeight: 600,
              fontSize: '0.95rem',
              backgroundColor: tab === 'login' ? '#ffffff' : 'transparent',
              color: tab === 'login' ? 'var(--primary)' : 'var(--text-muted)',
              boxShadow: tab === 'login' ? 'var(--shadow-sm)' : 'none',
              transition: 'all 0.2s',
            }}
          >
            Login
          </button>
          <button
            type="button"
            onClick={() => { setTab('register'); setError(''); setSuccessMsg(''); }}
            style={{
              flex: 1,
              padding: '0.6rem',
              borderRadius: '6px',
              fontWeight: 600,
              fontSize: '0.95rem',
              backgroundColor: tab === 'register' ? '#ffffff' : 'transparent',
              color: tab === 'register' ? 'var(--primary)' : 'var(--text-muted)',
              boxShadow: tab === 'register' ? 'var(--shadow-sm)' : 'none',
              transition: 'all 0.2s',
            }}
          >
            Register
          </button>
        </div>

        {/* Alerts */}
        {error && (
          <div style={{
            display: 'flex',
            alignItems: 'center',
            gap: '0.5rem',
            padding: '0.75rem 1rem',
            backgroundColor: '#fef2f2',
            color: 'var(--danger)',
            borderRadius: '6px',
            fontSize: '0.9rem',
            marginBottom: '1.5rem',
            border: '1px solid #fecaca',
          }}>
            <AlertCircle size={18} />
            <span>{error}</span>
          </div>
        )}

        {successMsg && (
          <div style={{
            display: 'flex',
            alignItems: 'center',
            gap: '0.5rem',
            padding: '0.75rem 1rem',
            backgroundColor: '#f0fdf4',
            color: 'var(--success)',
            borderRadius: '6px',
            fontSize: '0.9rem',
            marginBottom: '1.5rem',
            border: '1px solid #bbf7d0',
          }}>
            <CheckCircle2 size={18} />
            <span>{successMsg}</span>
          </div>
        )}

        {/* LOGIN FORM */}
        {tab === 'login' ? (
          <form onSubmit={handleLoginSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1.25rem' }}>
            <div>
              <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, marginBottom: '0.35rem' }}>
                User ID
              </label>
              <input
                type="text"
                required
                value={loginData.rollNumber}
                onChange={(e) => setLoginData({ ...loginData, rollNumber: e.target.value })}
                style={{
                  width: '100%',
                  padding: '0.7rem 0.9rem',
                  border: '1px solid var(--border)',
                  borderRadius: '6px',
                  fontSize: '0.95rem',
                  outline: 'none',
                }}
              />
            </div>

            <div>
              <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, marginBottom: '0.35rem' }}>
                Password
              </label>
              <div style={{ position: 'relative' }}>
                <input
                  type={showPassword ? 'text' : 'password'}
                  required
                  value={loginData.password}
                  onChange={(e) => setLoginData({ ...loginData, password: e.target.value })}
                  style={{
                    width: '100%',
                    padding: '0.7rem 2.5rem 0.7rem 0.9rem',
                    border: '1px solid var(--border)',
                    borderRadius: '6px',
                    fontSize: '0.95rem',
                    outline: 'none',
                  }}
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  style={{
                    position: 'absolute',
                    right: '0.75rem',
                    top: '50%',
                    transform: 'translateY(-50%)',
                    color: 'var(--text-muted)',
                  }}
                >
                  {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
                </button>
              </div>
            </div>

            <button
              type="submit"
              disabled={loading}
              className="btn btn-primary"
              style={{ padding: '0.75rem', marginTop: '0.5rem', width: '100%' }}
            >
              <LogIn size={18} />
              {loading ? 'Logging in...' : 'Sign In'}
            </button>
          </form>
        ) : (
          /* REGISTER FORM */
          <form onSubmit={handleRegisterSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
            <div>
              <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, marginBottom: '0.25rem' }}>
                Full Name
              </label>
              <input
                type="text"
                required
                value={registerData.name}
                onChange={(e) => setRegisterData({ ...registerData, name: e.target.value })}
                style={{ width: '100%', padding: '0.65rem 0.85rem', border: '1px solid var(--border)', borderRadius: '6px' }}
              />
            </div>

            <div>
              <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, marginBottom: '0.25rem' }}>
                Email
              </label>
              <input
                type="email"
                required
                value={registerData.email}
                onChange={(e) => setRegisterData({ ...registerData, email: e.target.value })}
                style={{ width: '100%', padding: '0.65rem 0.85rem', border: '1px solid var(--border)', borderRadius: '6px' }}
              />
            </div>

            <div>
              <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, marginBottom: '0.25rem' }}>
                Roll Number
              </label>
              <input
                type="text"
                required
                value={registerData.rollNumber}
                onChange={(e) => setRegisterData({ ...registerData, rollNumber: e.target.value })}
                style={{ width: '100%', padding: '0.65rem 0.85rem', border: '1px solid var(--border)', borderRadius: '6px' }}
              />
            </div>

            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0.75rem' }}>
              <div>
                <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, marginBottom: '0.25rem' }}>
                  Department
                </label>
                <select
                  required
                  value={registerData.departmentId}
                  onChange={(e) => setRegisterData({ ...registerData, departmentId: e.target.value })}
                  style={{ width: '100%', padding: '0.65rem 0.5rem', border: '1px solid var(--border)', borderRadius: '6px' }}
                >
                  <option value="">Select</option>
                  {departments.map((dept) => (
                    <option key={dept.id} value={dept.id}>
                      {dept.departmentName}
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, marginBottom: '0.25rem' }}>
                  Batch
                </label>
                <select
                  required
                  value={registerData.batchId}
                  onChange={(e) => setRegisterData({ ...registerData, batchId: e.target.value })}
                  style={{ width: '100%', padding: '0.65rem 0.5rem', border: '1px solid var(--border)', borderRadius: '6px' }}
                >
                  <option value="">Select</option>
                  {batches.map((b) => (
                    <option key={b.id} value={b.id}>
                      {b.batchName}
                    </option>
                  ))}
                </select>
              </div>
            </div>

            <div>
              <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, marginBottom: '0.25rem' }}>
                Password
              </label>
              <input
                type="password"
                required
                placeholder="Create password"
                value={registerData.password}
                onChange={(e) => setRegisterData({ ...registerData, password: e.target.value })}
                style={{ width: '100%', padding: '0.65rem 0.85rem', border: '1px solid var(--border)', borderRadius: '6px' }}
              />
            </div>

            <div>
              <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, marginBottom: '0.25rem' }}>
                Confirm Password
              </label>
              <input
                type="password"
                required
                placeholder="Confirm password"
                value={registerData.confirmPassword}
                onChange={(e) => setRegisterData({ ...registerData, confirmPassword: e.target.value })}
                style={{ width: '100%', padding: '0.65rem 0.85rem', border: '1px solid var(--border)', borderRadius: '6px' }}
              />
            </div>

            <button
              type="submit"
              disabled={loading}
              className="btn btn-primary"
              style={{ padding: '0.75rem', marginTop: '0.5rem', width: '100%' }}
            >
              <UserPlus size={18} />
              {loading ? 'Registering...' : 'Complete Registration'}
            </button>
          </form>
        )}
      </div>
    </div>
  );
}

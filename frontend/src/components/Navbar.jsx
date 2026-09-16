import React from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { 
  PlusCircle,
  LogOut, 
  LogIn, 
  UserCircle 
} from 'lucide-react';

export default function Navbar() {
  const { user, role, isAuthenticated, isAdmin, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const isAuthPage = location.pathname === '/auth';

  const handleLogout = () => {
    logout();
    navigate('/auth');
  };

  return (
    <nav style={{
      backgroundColor: '#ffffff',
      borderBottom: '1px solid var(--border)',
      position: 'sticky',
      top: 0,
      zIndex: 50,
      boxShadow: 'var(--shadow-sm)'
    }}>
      <div style={{
        maxWidth: '1360px',
        margin: '0 auto',
        padding: '0 2rem',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        height: '4.25rem'
      }}>
        {/* Brand Logo */}
        <Link to="/" style={{ display: 'flex', alignItems: 'center', textDecoration: 'none' }}>
          <span style={{ fontSize: '1.25rem', fontWeight: 800, color: 'var(--text-main)', letterSpacing: '-0.02em' }}>
            Interview<span style={{ color: 'var(--primary)' }}>Insights</span>
          </span>
        </Link>

        {/* Right-aligned Actions & User Controls */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '1.25rem' }}>
          {isAuthenticated ? (
            <>
              {!isAdmin && (
                <Link 
                  to="/submit-experience" 
                  style={{ 
                    display: 'flex', 
                    alignItems: 'center', 
                    gap: '0.45rem', 
                    padding: '0.45rem 0.75rem',
                    fontSize: '0.9rem',
                    fontWeight: 600,
                    color: 'var(--primary)',
                    borderRadius: '6px',
                    transition: 'background-color 0.15s ease',
                    textDecoration: 'none'
                  }}
                  onMouseEnter={(e) => e.currentTarget.style.backgroundColor = 'var(--primary-light)'}
                  onMouseLeave={(e) => e.currentTarget.style.backgroundColor = 'transparent'}
                >
                  <PlusCircle size={18} />
                  Share Experience
                </Link>
              )}

              <div style={{
                height: '1.5rem',
                width: '1px',
                backgroundColor: 'var(--border)'
              }} />

              <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', color: 'var(--text-main)', fontWeight: 500, fontSize: '0.95rem' }}>
                <UserCircle size={20} color="var(--primary)" />
                <span>{user?.name || 'User'}</span>
              </div>

              <button 
                onClick={handleLogout} 
                className="btn btn-outline" 
                style={{ 
                  padding: '0.45rem 0.85rem', 
                  fontSize: '0.85rem', 
                  color: 'var(--danger)',
                  borderColor: 'var(--border)',
                  display: 'flex',
                  alignItems: 'center',
                  gap: '0.4rem'
                }}
              >
                <LogOut size={15} />
                Logout
              </button>
            </>
          ) : !isAuthPage ? (
            <Link to="/auth" className="btn btn-primary" style={{ padding: '0.5rem 1.25rem', display: 'flex', alignItems: 'center', gap: '0.4rem' }}>
              <LogIn size={17} />
              Login / Sign Up
            </Link>
          ) : null}
        </div>
      </div>
    </nav>
  );
}

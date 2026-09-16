import React, { useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { ArrowRight } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export default function Home() {
  const { isAuthenticated, isAdmin } = useAuth();
  const navigate = useNavigate();

  // Redirect admin users to the admin dashboard
  useEffect(() => {
    if (isAdmin) {
      navigate('/admin');
    }
  }, [isAdmin, navigate]);

  return (
    <div style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
      {/* Hero Section */}
      <section style={{
        flex: 1,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        padding: '5rem 0 6rem',
        background: 'linear-gradient(180deg, #ffffff 0%, var(--bg-main) 100%)'
      }}>
        <div className="container" style={{ textAlign: 'center', maxWidth: '850px' }}>

          <h1 style={{
            fontSize: '3.25rem',
            fontWeight: 800,
            color: 'var(--text-main)',
            lineHeight: 1.15,
            marginBottom: '1.25rem',
            letterSpacing: '-0.02em'
          }}>
            Learn From Real Campus Insights
          </h1>

          <p style={{
            fontSize: '1.2rem',
            color: 'var(--text-muted)',
            marginBottom: '2.5rem',
            lineHeight: 1.6
          }}>
            Explore real interview questions and experiences shared by seniors and peers.
          </p>

          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '1rem', flexWrap: 'wrap' }}>
            <Link to="/experiences" className="btn btn-primary" style={{ padding: '0.75rem 1.75rem', fontSize: '1rem' }}>
              Explore Experiences
              <ArrowRight size={18} />
            </Link>
            <Link to="/questions" className="btn btn-outline" style={{ padding: '0.75rem 1.75rem', fontSize: '1rem' }}>
              View Company-Wise Questions
            </Link>
          </div>

        </div>
      </section>

    </div>
  );
}


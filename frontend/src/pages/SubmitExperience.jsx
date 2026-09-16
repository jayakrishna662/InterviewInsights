import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { api } from '../api/client';
import { Send, CheckCircle2, AlertCircle } from 'lucide-react';

export default function SubmitExperience() {
  const { user, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const [companies, setCompanies] = useState([]);
  const [formData, setFormData] = useState({
    companyId: '',
    interviewYear: new Date().getFullYear(),
    result: 'SELECTED',
    codingExperience: '',
    technicalExperience: '',
    hrExperience: '',
    aptitudeExperience: '',
    gdExperience: '',
    overallSuggestions: '',
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/auth');
      return;
    }

    async function loadCompanies() {
      try {
        const compData = await api.get('/companies/active');
        setCompanies(compData || []);
      } catch (err) {
        console.error('Failed to load companies:', err);
      }
    }
    loadCompanies();
  }, [isAuthenticated, navigate]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.companyId) {
      setError('Please select a company');
      return;
    }

    setError('');
    setLoading(true);

    try {
      await api.post('/interview-experiences', {
        userId: user?.id,
        companyId: parseInt(formData.companyId, 10),
        interviewYear: parseInt(formData.interviewYear, 10),
        result: formData.result,
        codingExperience: formData.codingExperience.trim(),
        technicalExperience: formData.technicalExperience.trim(),
        hrExperience: formData.hrExperience.trim(),
        aptitudeExperience: formData.aptitudeExperience.trim(),
        gdExperience: formData.gdExperience.trim(),
        overallSuggestions: formData.overallSuggestions.trim(),
      });

      setSuccess(true);
      setTimeout(() => {
        navigate('/experiences');
      }, 2000);
    } catch (err) {
      setError(err.message || 'Failed to submit interview experience');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ flex: 1, backgroundColor: 'var(--bg-main)', padding: '3rem 0' }}>
      <div className="container" style={{ maxWidth: '800px' }}>
        <div className="card" style={{ padding: '2.5rem' }}>
          <div style={{ marginBottom: '2rem' }}>
            <h1 style={{ fontSize: '1.85rem', fontWeight: 800, margin: '0 0 0.5rem', color: 'var(--text-main)' }}>
              Share Your Interview Experience
            </h1>
          </div>

          {error && (
            <div style={{
              display: 'flex',
              alignItems: 'center',
              gap: '0.5rem',
              padding: '0.85rem 1rem',
              backgroundColor: '#fef2f2',
              color: 'var(--danger)',
              borderRadius: '8px',
              marginBottom: '1.5rem',
              border: '1px solid #fecaca'
            }}>
              <AlertCircle size={18} />
              <span>{error}</span>
            </div>
          )}

          {success && (
            <div style={{
              display: 'flex',
              alignItems: 'center',
              gap: '0.5rem',
              padding: '0.85rem 1rem',
              backgroundColor: '#f0fdf4',
              color: 'var(--success)',
              borderRadius: '8px',
              marginBottom: '1.5rem',
              border: '1px solid #bbf7d0'
            }}>
              <CheckCircle2 size={18} />
              <span>Experience submitted successfully! AI question extraction has been triggered in the background. Redirecting...</span>
            </div>
          )}

          <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
            {/* Row 1: Company, Year, Result */}
            <div style={{ display: 'grid', gridTemplateColumns: '2fr 1fr 1fr', gap: '1rem' }}>
              <div>
                <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, marginBottom: '0.35rem' }}>
                  Company Name *
                </label>
                <select
                  required
                  value={formData.companyId}
                  onChange={(e) => setFormData({ ...formData, companyId: e.target.value })}
                  style={{ width: '100%', padding: '0.65rem', borderRadius: '6px', border: '1px solid var(--border)', outline: 'none' }}
                >
                  <option value="">Select Company</option>
                  {companies.map((c) => (
                    <option key={c.id} value={c.id}>
                      {c.companyName}
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, marginBottom: '0.35rem' }}>
                  Year *
                </label>
                <input
                  type="number"
                  required
                  min="2018"
                  max={new Date().getFullYear() + 1}
                  value={formData.interviewYear}
                  onChange={(e) => setFormData({ ...formData, interviewYear: e.target.value })}
                  style={{ width: '100%', padding: '0.65rem', borderRadius: '6px', border: '1px solid var(--border)' }}
                />
              </div>

              <div>
                <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, marginBottom: '0.35rem' }}>
                  Outcome *
                </label>
                <select
                  value={formData.result}
                  onChange={(e) => setFormData({ ...formData, result: e.target.value })}
                  style={{ width: '100%', padding: '0.65rem', borderRadius: '6px', border: '1px solid var(--border)' }}
                >
                  <option value="SELECTED">Selected</option>
                  <option value="REJECTED">Rejected</option>
                </select>
              </div>
            </div>

            {/* Coding Round */}
            <div>
              <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, marginBottom: '0.35rem' }}>
                Coding Round
              </label>
              <textarea
                rows={3}
                value={formData.codingExperience}
                onChange={(e) => setFormData({ ...formData, codingExperience: e.target.value })}
                style={{ width: '100%', padding: '0.75rem', borderRadius: '6px', border: '1px solid var(--border)', fontSize: '0.95rem' }}
              />
            </div>

            {/* Technical Round */}
            <div>
              <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, marginBottom: '0.35rem' }}>
                Technical Interview Round
              </label>
              <textarea
                rows={3}
                value={formData.technicalExperience}
                onChange={(e) => setFormData({ ...formData, technicalExperience: e.target.value })}
                style={{ width: '100%', padding: '0.75rem', borderRadius: '6px', border: '1px solid var(--border)', fontSize: '0.95rem' }}
              />
            </div>

            {/* HR Round */}
            <div>
              <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, marginBottom: '0.35rem' }}>
                HR / Behavioral Round
              </label>
              <textarea
                rows={2}
                value={formData.hrExperience}
                onChange={(e) => setFormData({ ...formData, hrExperience: e.target.value })}
                style={{ width: '100%', padding: '0.75rem', borderRadius: '6px', border: '1px solid var(--border)', fontSize: '0.95rem' }}
              />
            </div>

            {/* Aptitude & GD */}
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
              <div>
                <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, marginBottom: '0.35rem' }}>
                  Aptitude Round (Optional)
                </label>
                <textarea
                  rows={2}
                  value={formData.aptitudeExperience}
                  onChange={(e) => setFormData({ ...formData, aptitudeExperience: e.target.value })}
                  style={{ width: '100%', padding: '0.75rem', borderRadius: '6px', border: '1px solid var(--border)' }}
                />
              </div>

              <div>
                <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, marginBottom: '0.35rem' }}>
                  Group Discussion (Optional)
                </label>
                <textarea
                  rows={2}
                  value={formData.gdExperience}
                  onChange={(e) => setFormData({ ...formData, gdExperience: e.target.value })}
                  style={{ width: '100%', padding: '0.75rem', borderRadius: '6px', border: '1px solid var(--border)' }}
                />
              </div>
            </div>

            {/* Suggestions */}
            <div>
              <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, marginBottom: '0.35rem' }}>
                Overall Suggestions & Advice for Juniors
              </label>
              <textarea
                rows={2}
                value={formData.overallSuggestions}
                onChange={(e) => setFormData({ ...formData, overallSuggestions: e.target.value })}
                style={{ width: '100%', padding: '0.75rem', borderRadius: '6px', border: '1px solid var(--border)', fontSize: '0.95rem' }}
              />
            </div>

            <button
              type="submit"
              disabled={loading || success}
              className="btn btn-primary"
              style={{ padding: '0.85rem', fontSize: '1rem', marginTop: '0.5rem' }}
            >
              <Send size={18} />
              {loading ? 'Submitting Experience...' : 'Submit Interview Experience'}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}

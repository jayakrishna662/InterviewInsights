import React, { useState, useEffect } from 'react';
import { api } from '../api/client';

export default function Questions() {
  const [selectedCompany, setSelectedCompany] = useState('');
  const [companies, setCompanies] = useState([]);

  const [selectedDuration, setSelectedDuration] = useState('all');
  const [selectedCategory, setSelectedCategory] = useState('ALL');
  const [questions, setQuestions] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [hasSearched, setHasSearched] = useState(false);

  useEffect(() => {
    async function loadCompanies() {
      try {
        const data = await api.get('/companies/active');
        setCompanies(data || []);
      } catch (err) {
        setError(err.message || 'Failed to load companies');
      }
    }

    loadCompanies();
  }, []);

  async function handleApplyFilters() {
    try {
      setLoading(true);
      setError('');

      if (!selectedCompany) {
        setError('Please select a company');
        return;
      }

      const params = new URLSearchParams();

      if (selectedCategory !== 'ALL') {
        params.append('category', selectedCategory);
      }

      if (selectedDuration !== 'all') {
        params.append('years', selectedDuration);
      }

      const queryString = params.toString();

      const data = await api.get(
        `/questions/company/${selectedCompany}/frequency${queryString ? `?${queryString}` : ''
        }`
      );

      setQuestions(data || []);
      setHasSearched(true);
    } catch (err) {
      setError(err.message || 'Failed to load questions');
    } finally {
      setLoading(false);
    }
  }

  return (
    <div style={{ flex: 1, backgroundColor: 'var(--bg-main)', padding: '3rem 0' }}>
      <div className="container">
        {/* Header */}
        <div style={{ marginBottom: '2.5rem' }}>
          <h1 style={{ fontSize: '2rem', fontWeight: 800, margin: '0 0 0.5rem', color: 'var(--text-main)' }}>
            View Company-Wise Questions
          </h1>
        </div>

        {/* Company Filter */}
        <div style={{
          marginBottom: '1.5rem',
          maxWidth: '100%'
        }}>
          <label
            style={{
              display: 'block',
              marginBottom: '0.5rem',
              fontWeight: 600,
              color: 'var(--text-main)'
            }}
          >
            Company
          </label>

          <select
            value={selectedCompany}
            onChange={(e) => setSelectedCompany(e.target.value)}
            style={{
              width: '100%',
              padding: '0.75rem 1rem',
              border: '1px solid var(--border)',
              borderRadius: '8px',
              backgroundColor: '#ffffff',
              color: 'var(--text-main)',
              fontSize: '0.95rem'
            }}
          >
            <option value="">Select Company</option>

            {companies.map((company) => (
              <option key={company.id} value={company.id}>
                {company.companyName}
              </option>
            ))}
          </select>
        </div>

        {/* Duration Filter */}
        <div style={{
          marginBottom: '1.5rem'
        }}>
          <label
            style={{
              display: 'block',
              marginBottom: '0.5rem',
              fontWeight: 600,
              color: 'var(--text-main)'
            }}
          >
            Duration:
          </label>

          <select
            value={selectedDuration}
            onChange={(e) => setSelectedDuration(e.target.value)}
            style={{
              width: '100%',
              padding: '0.75rem 1rem',
              border: '1px solid var(--border)',
              borderRadius: '8px',
              backgroundColor: '#ffffff',
              color: 'var(--text-main)',
              fontSize: '0.95rem'
            }}
          >
            <option value="all">All time</option>
            <option value="1">Last 1 year</option>
            <option value="2">Last 2 years</option>
            <option value="3">Last 3 years</option>
            <option value="5">Last 5 years</option>
          </select>
        </div>

        {/* Category Filter */}
        <div style={{
          marginBottom: '1.5rem'
        }}>
          <label
            style={{
              display: 'block',
              marginBottom: '0.5rem',
              fontWeight: 600,
              color: 'var(--text-main)'
            }}
          >
            Filter by Category:
          </label>

          <select
            value={selectedCategory}
            onChange={(e) => setSelectedCategory(e.target.value)}
            style={{
              width: '100%',
              padding: '0.75rem 1rem',
              border: '1px solid var(--border)',
              borderRadius: '8px',
              backgroundColor: '#ffffff',
              color: 'var(--text-main)',
              fontSize: '0.95rem'
            }}
          >
            <option value="ALL">All Categories</option>
            <option value="CODING">Coding</option>
            <option value="TECHNICAL">Technical</option>
            <option value="APTITUDE">Aptitude</option>
            <option value="GD">Group Discussion</option>
            <option value="HR">HR</option>
          </select>
        </div>

        {/* Apply Filters Button */}
        <button
          className="btn"
          onClick={handleApplyFilters}
          style={{
            backgroundColor: 'var(--primary)',
            color: '#ffffff',
            border: '1px solid var(--primary)',
            padding: '0.7rem 1.5rem',
            marginBottom: '2rem'
          }}
        >
          Apply Filters
        </button>

        {/* Questions List */}
        {loading ? (
          <div style={{ textAlign: 'center', padding: '3rem', color: 'var(--text-muted)' }}>
            Loading extracted questions...
          </div>
        ) : !hasSearched ? (
          <div style={{ textAlign: 'center', padding: '3rem', color: 'var(--text-muted)' }}>
            Select a company and apply filters to view questions.
          </div>
        ) : questions.length === 0 ? (
          <div className="card" style={{ textAlign: 'center', padding: '3rem' }}>
            <p style={{ color: 'var(--text-muted)' }}>
              No questions found for the selected filters.
            </p>
          </div>
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
            {questions.map((q, idx) => (
              <div
                key={q.id || idx}
                className="card"
                style={{
                  display: 'flex',
                  justifyContent: 'space-between',
                  alignItems: 'center',
                  padding: '1.25rem 1.5rem',
                  gap: '1rem'
                }}
              >
                <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
                  <span style={{
                    color: 'var(--text-muted)',
                    fontWeight: 700,
                    fontSize: '0.9rem',
                    minWidth: '2rem'
                  }}>
                    #{idx + 1}
                  </span>
                  <p style={{ fontWeight: 600, color: 'var(--text-main)', margin: 0, fontSize: '1rem' }}>
                    {q.questionText}
                  </p>
                </div>

                <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
                  <span className="badge" style={{
                    backgroundColor: 'var(--primary-light)',
                    color: 'var(--primary)',
                    fontSize: '0.75rem'
                  }}>
                    {q.category}
                  </span>
                  <span className="badge">
                    [{q.frequency}]
                  </span>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

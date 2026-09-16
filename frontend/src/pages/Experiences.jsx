import React, { useState, useEffect } from 'react';
import { useSearchParams, Link } from 'react-router-dom';
import { api } from '../api/client';
import { FileText, Building2, Calendar, CheckCircle2, XCircle, PlusCircle, Filter } from 'lucide-react';

export default function Experiences() {
  const [searchParams, setSearchParams] = useSearchParams();
  const selectedCompanyId = searchParams.get('companyId') || '';

  const [experiences, setExperiences] = useState([]);
  const [companies, setCompanies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [expandedExperienceId, setExpandedExperienceId] = useState(null);

  useEffect(() => {
    async function loadData() {
      try {
        setLoading(true);
        const [expData, compData] = await Promise.all([
          selectedCompanyId ? api.get(`/interview-experiences/company/${selectedCompanyId}`) : api.get('/interview-experiences'),
          api.get('/companies/active'),
        ]);
        setExperiences(expData || []);
        setCompanies(compData || []);
      } catch (err) {
        setError(err.message || 'Failed to load experiences');
      } finally {
        setLoading(false);
      }
    }
    loadData();
  }, [selectedCompanyId]);

  const handleCompanyFilterChange = (e) => {
    const val = e.target.value;
    if (val) {
      setSearchParams({ companyId: val });
    } else {
      setSearchParams({});
    }
  };

  const getExperiencePreview = (exp) => {
    const parts = [
      exp.codingExperience,
      exp.technicalExperience,
      exp.hrExperience,
      exp.gdExperience,
      exp.aptitudeExperience,
      exp.overallSuggestions
    ].filter(Boolean);

    const text = parts.join(' ');

    if (text.length <= 250) {
      return text;
    }

    return text.substring(0, 250).trim() + '...';
  };

  return (
    <div style={{ flex: 1, backgroundColor: 'var(--bg-main)', padding: '3rem 0' }}>
      <div className="container">
        {/* Header */}
        <div style={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          flexWrap: 'wrap',
          gap: '1rem',
          marginBottom: '2.5rem'
        }}>
          <div>
            <h1 style={{ fontSize: '2rem', fontWeight: 800, margin: '0 0 0.5rem', color: 'var(--text-main)' }}>
              Interview Experiences
            </h1>
            <p style={{ color: 'var(--text-muted)' }}>
              Read authentic campus placement experiences and preparation strategies from seniors.
            </p>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', flexWrap: 'wrap' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <Filter size={18} color="var(--text-muted)" />
              <select
                value={selectedCompanyId}
                onChange={handleCompanyFilterChange}
                style={{
                  padding: '0.6rem 1rem',
                  borderRadius: '8px',
                  border: '1px solid var(--border)',
                  backgroundColor: '#ffffff',
                  fontSize: '0.9rem',
                  outline: 'none'
                }}
              >
                <option value="">All Companies</option>
                {companies.map((c) => (
                  <option key={c.id} value={c.id}>
                    {c.companyName}
                  </option>
                ))}
              </select>
            </div>

            <Link to="/submit-experience" className="btn btn-primary" style={{ padding: '0.6rem 1.25rem' }}>
              <PlusCircle size={18} />
              Share Experience
            </Link>
          </div>
        </div>

        {error && (
          <div style={{
            padding: '1rem',
            backgroundColor: '#fef2f2',
            color: 'var(--danger)',
            borderRadius: '8px',
            marginBottom: '1.5rem',
            border: '1px solid #fecaca'
          }}>
            {error}
          </div>
        )}

        {/* Experience Cards */}
        {loading ? (
          <div style={{ textAlign: 'center', padding: '3rem', color: 'var(--text-muted)' }}>
            Loading interview experiences...
          </div>
        ) : experiences.length === 0 ? (
          <div className="card" style={{ textAlign: 'center', padding: '3rem' }}>
            <FileText size={40} style={{ color: 'var(--text-muted)', marginBottom: '1rem' }} />
            <h3 style={{ fontSize: '1.2rem', marginBottom: '0.5rem' }}>No Experiences Found</h3>
            <p style={{ color: 'var(--text-muted)', marginBottom: '1.5rem' }}>
              Be the first to share an interview experience for this company!
            </p>
            <Link to="/submit-experience" className="btn btn-primary">
              Share Your Experience
            </Link>
          </div>
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
            {experiences.map((exp) => (
              <div key={exp.id} className="card" style={{ padding: '2rem' }}>
                <div style={{
                  display: 'flex',
                  justifyContent: 'space-between',
                  alignItems: 'flex-start',
                  flexWrap: 'wrap',
                  gap: '0.75rem',
                  marginBottom: '1rem',
                  borderBottom: '1px solid var(--border)',
                  paddingBottom: '1rem'
                }}>
                  <div>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '0.6rem', marginBottom: '0.25rem' }}>
                      <Building2 size={20} color="var(--primary)" />
                      <h2 style={{ fontSize: '1.35rem', fontWeight: 700, margin: 0 }}>
                        {exp.companyName || 'Company'}
                      </h2>
                    </div>
                    <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>
                      Candidate: <strong>{exp.userName || 'Anonymous'}</strong>
                    </p>
                  </div>

                  <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
                    <span style={{
                      display: 'inline-flex',
                      alignItems: 'center',
                      gap: '0.35rem',
                      padding: '0.35rem 0.75rem',
                      borderRadius: '9999px',
                      fontSize: '0.8rem',
                      fontWeight: 600,
                      backgroundColor: exp.result === 'SELECTED' ? '#dcfce7' : '#fee2e2',
                      color: exp.result === 'SELECTED' ? '#15803d' : '#b91c1c'
                    }}>
                      {exp.result === 'SELECTED' ? <CheckCircle2 size={14} /> : <XCircle size={14} />}
                      {exp.result}
                    </span>

                    <span style={{
                      display: 'inline-flex',
                      alignItems: 'center',
                      gap: '0.35rem',
                      color: 'var(--text-muted)',
                      fontSize: '0.85rem'
                    }}>
                      <Calendar size={14} />
                      {exp.interviewYear}
                    </span>
                  </div>
                </div>

                {/* Experience Rounds Breakdown */}
                {expandedExperienceId === exp.id ? (
                  <>
                    {/* Full Experience */}
                    <div style={{
                      display: 'flex',
                      flexDirection: 'column',
                      gap: '1.25rem'
                    }}>

                      {exp.codingExperience && (
                        <div>
                          <h4 style={{
                            fontSize: '0.95rem',
                            fontWeight: 600,
                            color: 'var(--primary)',
                            marginBottom: '0.25rem'
                          }}>
                            Coding / Online Assessment Round:
                          </h4>

                          <p style={{
                            color: 'var(--text-main)',
                            fontSize: '0.95rem',
                            whiteSpace: 'pre-wrap'
                          }}>
                            {exp.codingExperience}
                          </p>
                        </div>
                      )}

                      {exp.technicalExperience && (
                        <div>
                          <h4 style={{
                            fontSize: '0.95rem',
                            fontWeight: 600,
                            color: '#059669',
                            marginBottom: '0.25rem'
                          }}>
                            Technical Interview:
                          </h4>

                          <p style={{
                            color: 'var(--text-main)',
                            fontSize: '0.95rem',
                            whiteSpace: 'pre-wrap'
                          }}>
                            {exp.technicalExperience}
                          </p>
                        </div>
                      )}

                      {exp.hrExperience && (
                        <div>
                          <h4 style={{
                            fontSize: '0.95rem',
                            fontWeight: 600,
                            color: '#7c3aed',
                            marginBottom: '0.25rem'
                          }}>
                            HR Round:
                          </h4>

                          <p style={{
                            color: 'var(--text-main)',
                            fontSize: '0.95rem',
                            whiteSpace: 'pre-wrap'
                          }}>
                            {exp.hrExperience}
                          </p>
                        </div>
                      )}

                      {exp.aptitudeExperience && (
                        <div>
                          <h4 style={{
                            fontSize: '0.95rem',
                            fontWeight: 600,
                            color: '#d97706',
                            marginBottom: '0.25rem'
                          }}>
                            Aptitude Round:
                          </h4>

                          <p style={{
                            color: 'var(--text-main)',
                            fontSize: '0.95rem',
                            whiteSpace: 'pre-wrap'
                          }}>
                            {exp.aptitudeExperience}
                          </p>
                        </div>
                      )}
                      {exp.gdExperience && (
                                  <div>
                                    <h4 style={{
                                      fontSize: '0.95rem',
                                      fontWeight: 600,
                                      color: '#2563eb',
                                      marginBottom: '0.25rem'
                                    }}>
                                      Group Discussion Round:
                                    </h4>

                                    <p style={{
                                      color: 'var(--text-main)',
                                      fontSize: '0.95rem',
                                      whiteSpace: 'pre-wrap'
                                    }}>
                                      {exp.gdExperience}
                                    </p>
                                </div>
                              )}


                      {exp.overallSuggestions && (
                        <div style={{
                          backgroundColor: 'var(--bg-main)',
                          padding: '1rem',
                          borderRadius: '8px',
                          borderLeft: '4px solid var(--primary)'
                        }}>
                          <h4 style={{
                            fontSize: '0.9rem',
                            fontWeight: 600,
                            marginBottom: '0.25rem'
                          }}>
                            Advice & Suggestions for Juniors:
                          </h4>

                          <p style={{
                            color: 'var(--text-main)',
                            fontSize: '0.9rem',
                            fontStyle: 'italic'
                          }}>
                            "{exp.overallSuggestions}"
                          </p>
                        </div>
                      )}
                    </div>

                    {/* Show Less */}
                    <button
                      onClick={() => setExpandedExperienceId(null)}
                      className="btn"
                      style={{
                        marginTop: '1.5rem',
                        backgroundColor: 'transparent',
                        color: 'var(--primary)',
                        border: '1px solid var(--primary)'
                      }}
                    >
                      Show Less
                    </button>
                  </>
                ) : (
                  <>
                    {/* Experience Glimpse */}
                    <p style={{
                      color: 'var(--text-main)',
                      fontSize: '0.95rem',
                      lineHeight: 1.6,
                      margin: '0 0 1rem'
                    }}>
                      {getExperiencePreview(exp)}
                    </p>

                    {/* See More */}
                    <button
                      onClick={() => setExpandedExperienceId(exp.id)}
                      className="btn"
                      style={{
                        backgroundColor: 'transparent',
                        color: 'var(--primary)',
                        border: '1px solid var(--primary)'
                      }}
                    >
                      See More →
                    </button>
                  </>
                )}
              </div>
            ))}
          </div>
        )}
      </div>
    </div >
  );
}

import React, { useState, useEffect } from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { api } from '../api/client';
import {
  Building2,
  FileText,
  Users,
  Plus,
  Edit3,
  Power,
  CheckCircle2,
  AlertCircle,
  X,
  Search,
  RefreshCw,
  Clock3,
  Loader2
} from 'lucide-react';

export default function AdminDashboard() {
  const { isAdmin, loading: authLoading } = useAuth();

  const [stats, setStats] = useState({
    totalCompanies: 0,
    totalExperiences: 0,
    totalStudents: 0
  });

  const [companies, setCompanies] = useState([]);
  const [aiExperiences, setAiExperiences] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');

  // Modals / action states
  const [showAddModal, setShowAddModal] = useState(false);
  const [newCompanyName, setNewCompanyName] = useState('');
  const [editingCompany, setEditingCompany] = useState(null);
  const [editCompanyName, setEditCompanyName] = useState('');

  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  // Load stats, companies and AI processing status
  useEffect(() => {
    if (isAdmin) {
      fetchAdminData();
    }
  }, [isAdmin]);

  const handleRetryAi = async (experienceId) => {
    try {
      await api.post(`/admin/experiences/${experienceId}/ai/retry`);

      await fetchAdminData();
    } catch (err) {
      console.error('Failed to retry AI processing:', err);
      alert('Failed to retry AI processing');
    }
  };

  const fetchAdminData = async () => {
    try {
      setLoading(true);

      const [statsData, companiesData, aiStatusData] = await Promise.all([
        api.get('/admin/stats'),
        api.get('/companies'),
        api.get('/admin/experiences/ai-status'),
      ]);

      setStats(
        statsData || {
          totalCompanies: 0,
          totalExperiences: 0,
          totalStudents: 0
        }
      );

      setCompanies(companiesData || []);
      setAiExperiences(aiStatusData || []);
    } catch (err) {
      setError(err.message || 'Failed to load admin data');
    } finally {
      setLoading(false);
    }
  };

  // Add Company
  const handleAddCompany = async (e) => {
    e.preventDefault();

    if (!newCompanyName.trim()) return;

    setError('');
    setSuccess('');

    try {
      await api.post('/companies', {
        companyName: newCompanyName.trim()
      });

      setSuccess(
        `Company "${newCompanyName.trim()}" added successfully!`
      );

      setNewCompanyName('');
      setShowAddModal(false);

      fetchAdminData();
    } catch (err) {
      setError(err.message || 'Failed to add company');
    }
  };

  // Edit Company
  const handleEditCompany = async (e) => {
    e.preventDefault();

    if (!editCompanyName.trim() || !editingCompany) return;

    setError('');
    setSuccess('');

    try {
      await api.put(`/companies/${editingCompany.id}`, {
        companyName: editCompanyName.trim()
      });

      setSuccess(
        `Company updated to "${editCompanyName.trim()}"`
      );

      setEditingCompany(null);

      fetchAdminData();
    } catch (err) {
      setError(err.message || 'Failed to update company');
    }
  };

  // Soft delete / Toggle active status
  const handleToggleStatus = async (company) => {
    setError('');
    setSuccess('');

    try {
      const updated = await api.patch(
        `/companies/${company.id}/toggle-status`
      );

      setSuccess(
        `Company "${company.companyName}" is now ${
          updated.active ? 'Active' : 'Deactivated'
        }`
      );

      fetchAdminData();
    } catch (err) {
      setError(err.message || 'Failed to change company status');
    }
  };

  // Guard: if done loading auth and not admin, redirect
  if (!authLoading && !isAdmin) {
    return <Navigate to="/" replace />;
  }

  const filteredCompanies = companies.filter((c) =>
    c.companyName
      .toLowerCase()
      .includes(searchTerm.toLowerCase())
  );

  const getStatusConfig = (status) => {
    switch (status) {
      case 'COMPLETED':
        return {
          label: 'Completed',
          background: '#ecfdf5',
          color: '#047857',
          border: '#a7f3d0',
          icon: CheckCircle2
        };

      case 'PROCESSING':
        return {
          label: 'Processing',
          background: '#eff6ff',
          color: '#2563eb',
          border: '#bfdbfe',
          icon: Loader2
        };

      case 'FAILED':
        return {
          label: 'Failed',
          background: '#fef2f2',
          color: '#dc2626',
          border: '#fecaca',
          icon: AlertCircle
        };

      case 'PENDING':
      default:
        return {
          label: 'Pending',
          background: '#fffbeb',
          color: '#d97706',
          border: '#fde68a',
          icon: Clock3
        };
    }
  };

  return (
    <div
      style={{
        flex: 1,
        backgroundColor: 'var(--bg-main)',
        padding: '2.5rem 0'
      }}
    >
      <div className="container">

        {/* Header */}
        <div
          style={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'space-between',
            marginBottom: '2rem',
            flexWrap: 'wrap',
            gap: '1rem'
          }}
        >
          <div>
            <div
              style={{
                display: 'flex',
                alignItems: 'center',
                gap: '0.5rem',
                marginBottom: '0.25rem'
              }}
            >
              <h1
                style={{
                  fontSize: '2rem',
                  fontWeight: 800,
                  color: 'var(--text-main)',
                  margin: 0
                }}
              >
                Admin Portal
              </h1>
            </div>
          </div>

          <button
            onClick={() => setShowAddModal(true)}
            className="btn btn-primary"
            style={{
              padding: '0.7rem 1.25rem'
            }}
          >
            <Plus size={18} />
            Add Company
          </button>
        </div>

        {/* Alerts */}
        {error && (
          <div
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: '0.5rem',
              padding: '0.75rem 1rem',
              backgroundColor: '#fef2f2',
              color: 'var(--danger)',
              borderRadius: '8px',
              marginBottom: '1.5rem',
              border: '1px solid #fecaca'
            }}
          >
            <AlertCircle size={18} />
            <span>{error}</span>
          </div>
        )}

        {success && (
          <div
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: '0.5rem',
              padding: '0.75rem 1rem',
              backgroundColor: '#f0fdf4',
              color: 'var(--success)',
              borderRadius: '8px',
              marginBottom: '1.5rem',
              border: '1px solid #bbf7d0'
            }}
          >
            <CheckCircle2 size={18} />
            <span>{success}</span>
          </div>
        )}

        {/* Metrics Cards */}
        <div
          style={{
            display: 'grid',
            gridTemplateColumns:
              'repeat(auto-fit, minmax(260px, 1fr))',
            gap: '1.5rem',
            marginBottom: '2.5rem'
          }}
        >

          {/* Companies Count */}
          <div
            className="card"
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: '1.25rem'
            }}
          >
            <div
              style={{
                width: '3.5rem',
                height: '3.5rem',
                borderRadius: '12px',
                backgroundColor: 'var(--primary-light)',
                color: 'var(--primary)',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center'
              }}
            >
              <Building2 size={28} />
            </div>

            <div>
              <p
                style={{
                  color: 'var(--text-muted)',
                  fontSize: '0.9rem',
                  fontWeight: 500
                }}
              >
                Total Companies
              </p>

              <h2
                style={{
                  fontSize: '2rem',
                  fontWeight: 800,
                  color: 'var(--text-main)',
                  margin: 0
                }}
              >
                {loading ? '...' : stats.totalCompanies}
              </h2>
            </div>
          </div>

          {/* Experiences Count */}
          <div
            className="card"
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: '1.25rem'
            }}
          >
            <div
              style={{
                width: '3.5rem',
                height: '3.5rem',
                borderRadius: '12px',
                backgroundColor: '#ecfdf5',
                color: '#059669',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center'
              }}
            >
              <FileText size={28} />
            </div>

            <div>
              <p
                style={{
                  color: 'var(--text-muted)',
                  fontSize: '0.9rem',
                  fontWeight: 500
                }}
              >
                Interview Experiences
              </p>

              <h2
                style={{
                  fontSize: '2rem',
                  fontWeight: 800,
                  color: 'var(--text-main)',
                  margin: 0
                }}
              >
                {loading ? '...' : stats.totalExperiences}
              </h2>
            </div>
          </div>

          {/* Students Count */}
          <div
            className="card"
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: '1.25rem'
            }}
          >
            <div
              style={{
                width: '3.5rem',
                height: '3.5rem',
                borderRadius: '12px',
                backgroundColor: '#fdf4ff',
                color: '#c026d3',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center'
              }}
            >
              <Users size={28} />
            </div>

            <div>
              <p
                style={{
                  color: 'var(--text-muted)',
                  fontSize: '0.9rem',
                  fontWeight: 500
                }}
              >
                Registered Students
              </p>

              <h2
                style={{
                  fontSize: '2rem',
                  fontWeight: 800,
                  color: 'var(--text-main)',
                  margin: 0
                }}
              >
                {loading ? '...' : stats.totalStudents}
              </h2>
            </div>
          </div>
        </div>

        {/* AI Processing Status */}
        <div
          className="card"
          style={{
            padding: '2rem',
            marginBottom: '2.5rem'
          }}
        >
          {/* Section Header */}
          <div
            style={{
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center',
              marginBottom: '1.5rem',
              gap: '1rem',
              flexWrap: 'wrap'
            }}
          >
            <div>
              <h2
                style={{
                  fontSize: '1.25rem',
                  fontWeight: 700,
                  color: 'var(--text-main)',
                  margin: 0
                }}
              >
                AI Processing Status
              </h2>

              <p
                style={{
                  margin: '0.35rem 0 0',
                  fontSize: '0.85rem',
                  color: 'var(--text-muted)'
                }}
              >
                Monitor AI question extraction for submitted experiences.
              </p>
            </div>

            <div
              style={{
                display: 'flex',
                alignItems: 'center',
                gap: '0.5rem',
                padding: '0.4rem 0.75rem',
                borderRadius: '9999px',
                backgroundColor: 'var(--bg-main)',
                color: 'var(--text-muted)',
                fontSize: '0.8rem',
                fontWeight: 600
              }}
            >
              {aiExperiences.length} Experiences
            </div>
          </div>

          {/* Table */}
          <div
            style={{
              overflowX: 'auto',
              border: '1px solid var(--border)',
              borderRadius: '10px'
            }}
          >
            <table
              style={{
                width: '100%',
                borderCollapse: 'collapse',
                minWidth: '760px',
                textAlign: 'left'
              }}
            >
              <thead>
                <tr
                  style={{
                    backgroundColor: 'var(--bg-main)',
                    borderBottom: '1px solid var(--border)'
                  }}
                >
                  <th
                    style={{
                      padding: '0.9rem 1rem',
                      color: 'var(--text-muted)',
                      fontSize: '0.75rem',
                      fontWeight: 700,
                      width: '15%'
                    }}
                  >
                    EXPERIENCE
                  </th>

                  <th
                    style={{
                      padding: '0.9rem 1rem',
                      color: 'var(--text-muted)',
                      fontSize: '0.75rem',
                      fontWeight: 700,
                      width: '25%'
                    }}
                  >
                    STUDENT
                  </th>

                  <th
                    style={{
                      padding: '0.9rem 1rem',
                      color: 'var(--text-muted)',
                      fontSize: '0.75rem',
                      fontWeight: 700,
                      width: '25%'
                    }}
                  >
                    COMPANY
                  </th>

                  <th
                    style={{
                      padding: '0.9rem 1rem',
                      color: 'var(--text-muted)',
                      fontSize: '0.75rem',
                      fontWeight: 700,
                      width: '20%'
                    }}
                  >
                    AI STATUS
                  </th>

                  <th
                    style={{
                      padding: '0.9rem 1rem',
                      color: 'var(--text-muted)',
                      fontSize: '0.75rem',
                      fontWeight: 700,
                      width: '15%',
                      textAlign: 'center'
                    }}
                  >
                    ACTION
                  </th>
                </tr>
              </thead>

              <tbody>
                {aiExperiences.length === 0 ? (
                  <tr>
                    <td
                      colSpan={5}
                      style={{
                        padding: '2.5rem 1rem',
                        textAlign: 'center',
                        color: 'var(--text-muted)'
                      }}
                    >
                      No interview experiences found.
                    </td>
                  </tr>
                ) : (
                  aiExperiences.map((experience) => {
                    const statusConfig = getStatusConfig(
                      experience.aiProcessingStatus
                    );

                    const StatusIcon = statusConfig.icon;

                    return (
                      <tr
                        key={experience.experienceId}
                        style={{
                          borderBottom: '1px solid var(--border)'
                        }}
                      >
                        {/* Experience ID */}
                        <td
                          style={{
                            padding: '1rem',
                            color: 'var(--text-muted)',
                            fontSize: '0.9rem',
                            fontWeight: 600
                          }}
                        >
                          #{experience.experienceId}
                        </td>

                        {/* Student */}
                        <td
                          style={{
                            padding: '1rem',
                            color: 'var(--text-main)',
                            fontSize: '0.9rem',
                            fontWeight: 600
                          }}
                        >
                          {experience.studentName}
                        </td>

                        {/* Company */}
                        <td
                          style={{
                            padding: '1rem',
                            color: 'var(--text-main)',
                            fontSize: '0.9rem'
                          }}
                        >
                          {experience.companyName}
                        </td>

                        {/* Status */}
                        <td
                          style={{
                            padding: '1rem'
                          }}
                        >
                          <span
                            style={{
                              display: 'inline-flex',
                              alignItems: 'center',
                              gap: '0.4rem',
                              padding: '0.4rem 0.7rem',
                              borderRadius: '9999px',
                              backgroundColor:
                                statusConfig.background,
                              color: statusConfig.color,
                              border:
                                `1px solid ${statusConfig.border}`,
                              fontSize: '0.75rem',
                              fontWeight: 700,
                              whiteSpace: 'nowrap'
                            }}
                          >
                            <StatusIcon size={14} />
                            {statusConfig.label}
                          </span>
                        </td>

                        {/* Action */}
                        <td
                          style={{
                            padding: '1rem',
                            textAlign: 'center'
                          }}
                        >
                          {experience.aiProcessingStatus === 'FAILED' ? (
    <button
        onClick={() => handleRetryAi(experience.experienceId)}
        style={{
            display: 'inline-flex',
            alignItems: 'center',
            gap: '7px',
            padding: '8px 14px',
            border: '1px solid #dbe3ef',
            borderRadius: '10px',
            background: '#fff',
            color: '#1f2937',
            fontSize: '13px',
            fontWeight: '500',
            cursor: 'pointer',
        }}
    >
        <RefreshCw size={15} />
        Retry
    </button>
) : experience.aiProcessingStatus === 'PENDING' ||
  experience.aiProcessingStatus === 'PROCESSING' ? (
    <span
        style={{
            fontSize: '13px',
            color: '#64748b',
            fontWeight: '500',
        }}
    >
        Processing...
    </span>
) : (
    <span style={{ color: '#64748b' }}>—</span>
)}
                        </td>
                      </tr>
                    );
                  })
                )}
              </tbody>
            </table>
          </div>
        </div>

        {/* Companies Management Section */}
        <div
          className="card"
          style={{
            padding: '2rem'
          }}
        >
          <div
            style={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between',
              marginBottom: '1.5rem',
              flexWrap: 'wrap',
              gap: '1rem'
            }}
          >
            {/* Search filter */}
            <div
              style={{
                position: 'relative',
                width: '280px'
              }}
            >
              <Search
                size={18}
                style={{
                  position: 'absolute',
                  left: '0.75rem',
                  top: '50%',
                  transform: 'translateY(-50%)',
                  color: 'var(--text-muted)'
                }}
              />

              <input
                type="text"
                placeholder="Search company..."
                value={searchTerm}
                onChange={(e) =>
                  setSearchTerm(e.target.value)
                }
                style={{
                  width: '100%',
                  padding:
                    '0.6rem 0.75rem 0.6rem 2.25rem',
                  borderRadius: '6px',
                  border:
                    '1px solid var(--border)',
                  fontSize: '0.9rem',
                  outline: 'none'
                }}
              />
            </div>
          </div>

          {/* Companies Table */}
          <div
            style={{
              overflowX: 'auto'
            }}
          >
            <table
              style={{
                width: '100%',
                borderCollapse: 'collapse',
                textAlign: 'left'
              }}
            >
              <thead>
                <tr
                  style={{
                    borderBottom:
                      '2px solid var(--border)',
                    color: 'var(--text-muted)',
                    fontSize: '0.85rem'
                  }}
                >
                  <th
                    style={{
                      padding: '0.75rem 1rem'
                    }}
                  >
                    ID
                  </th>

                  <th
                    style={{
                      padding: '0.75rem 1rem'
                    }}
                  >
                    COMPANY NAME
                  </th>

                  <th
                    style={{
                      padding: '0.75rem 1rem'
                    }}
                  >
                    STATUS
                  </th>

                  <th
                    style={{
                      padding: '0.75rem 1rem',
                      textAlign: 'right'
                    }}
                  >
                    ACTIONS
                  </th>
                </tr>
              </thead>

              <tbody>
                {filteredCompanies.length === 0 ? (
                  <tr>
                    <td
                      colSpan={4}
                      style={{
                        padding: '2rem',
                        textAlign: 'center',
                        color: 'var(--text-muted)'
                      }}
                    >
                      No companies found.
                    </td>
                  </tr>
                ) : (
                  filteredCompanies.map((company) => (
                    <tr
                      key={company.id}
                      style={{
                        borderBottom:
                          '1px solid var(--border)'
                      }}
                    >
                      <td
                        style={{
                          padding: '1rem',
                          color: 'var(--text-muted)',
                          fontSize: '0.9rem'
                        }}
                      >
                        #{company.id}
                      </td>

                      <td
                        style={{
                          padding: '1rem',
                          fontWeight: 600,
                          color: 'var(--text-main)'
                        }}
                      >
                        {company.companyName}
                      </td>

                      <td
                        style={{
                          padding: '1rem'
                        }}
                      >
                        <span
                          style={{
                            display: 'inline-block',
                            padding: '0.25rem 0.6rem',
                            borderRadius: '9999px',
                            fontSize: '0.75rem',
                            fontWeight: 600,
                            backgroundColor: company.active
                              ? '#dcfce7'
                              : '#f1f5f9',
                            color: company.active
                              ? '#166534'
                              : '#64748b'
                          }}
                        >
                          {company.active
                            ? 'Active'
                            : 'Inactive (Soft-Deleted)'}
                        </span>
                      </td>

                      <td
                        style={{
                          padding: '1rem',
                          textAlign: 'right'
                        }}
                      >
                        <div
                          style={{
                            display: 'inline-flex',
                            gap: '0.5rem'
                          }}
                        >
                          <button
                            onClick={() => {
                              setEditingCompany(company);
                              setEditCompanyName(
                                company.companyName
                              );
                            }}
                            className="btn btn-outline"
                            style={{
                              padding: '0.4rem 0.75rem',
                              fontSize: '0.85rem'
                            }}
                            title="Edit Name"
                          >
                            <Edit3 size={15} />
                            Edit
                          </button>

                          <button
                            onClick={() =>
                              handleToggleStatus(company)
                            }
                            className="btn btn-outline"
                            style={{
                              padding: '0.4rem 0.75rem',
                              fontSize: '0.85rem',
                              color: company.active
                                ? 'var(--danger)'
                                : 'var(--success)'
                            }}
                            title={
                              company.active
                                ? 'Soft Delete / Deactivate'
                                : 'Reactivate'
                            }
                          >
                            <Power size={15} />
                            {company.active
                              ? 'Deactivate'
                              : 'Activate'}
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {/* Add Company Modal */}
      {showAddModal && (
        <div
          style={{
            position: 'fixed',
            inset: 0,
            backgroundColor:
              'rgba(0, 0, 0, 0.5)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            zIndex: 100,
            padding: '1rem'
          }}
        >
          <div
            className="card"
            style={{
              width: '100%',
              maxWidth: '420px',
              padding: '2rem'
            }}
          >
            <div
              style={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                marginBottom: '1.25rem'
              }}
            >
              <h3
                style={{
                  fontSize: '1.25rem',
                  fontWeight: 700,
                  margin: 0
                }}
              >
                Add New Company
              </h3>

              <button
                onClick={() =>
                  setShowAddModal(false)
                }
                style={{
                  color: 'var(--text-muted)'
                }}
              >
                <X size={20} />
              </button>
            </div>

            <form
              onSubmit={handleAddCompany}
              style={{
                display: 'flex',
                flexDirection: 'column',
                gap: '1rem'
              }}
            >
              <div>
                <label
                  style={{
                    display: 'block',
                    fontSize: '0.85rem',
                    fontWeight: 600,
                    marginBottom: '0.35rem'
                  }}
                >
                  Company Name
                </label>

                <input
                  type="text"
                  required
                  placeholder="e.g. Goldman Sachs"
                  value={newCompanyName}
                  onChange={(e) =>
                    setNewCompanyName(e.target.value)
                  }
                  style={{
                    width: '100%',
                    padding: '0.65rem 0.85rem',
                    border:
                      '1px solid var(--border)',
                    borderRadius: '6px',
                    fontSize: '0.95rem'
                  }}
                />
              </div>

              <div
                style={{
                  display: 'flex',
                  justifyContent: 'flex-end',
                  gap: '0.75rem',
                  marginTop: '0.5rem'
                }}
              >
                <button
                  type="button"
                  onClick={() =>
                    setShowAddModal(false)
                  }
                  className="btn btn-outline"
                >
                  Cancel
                </button>

                <button
                  type="submit"
                  className="btn btn-primary"
                >
                  Add Company
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Edit Company Modal */}
      {editingCompany && (
        <div
          style={{
            position: 'fixed',
            inset: 0,
            backgroundColor:
              'rgba(0, 0, 0, 0.5)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            zIndex: 100,
            padding: '1rem'
          }}
        >
          <div
            className="card"
            style={{
              width: '100%',
              maxWidth: '420px',
              padding: '2rem'
            }}
          >
            <div
              style={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                marginBottom: '1.25rem'
              }}
            >
              <h3
                style={{
                  fontSize: '1.25rem',
                  fontWeight: 700,
                  margin: 0
                }}
              >
                Edit Company Name
              </h3>

              <button
                onClick={() =>
                  setEditingCompany(null)
                }
                style={{
                  color: 'var(--text-muted)'
                }}
              >
                <X size={20} />
              </button>
            </div>

            <form
              onSubmit={handleEditCompany}
              style={{
                display: 'flex',
                flexDirection: 'column',
                gap: '1rem'
              }}
            >
              <div>
                <label
                  style={{
                    display: 'block',
                    fontSize: '0.85rem',
                    fontWeight: 600,
                    marginBottom: '0.35rem'
                  }}
                >
                  Company Name
                </label>

                <input
                  type="text"
                  required
                  value={editCompanyName}
                  onChange={(e) =>
                    setEditCompanyName(e.target.value)
                  }
                  style={{
                    width: '100%',
                    padding: '0.65rem 0.85rem',
                    border:
                      '1px solid var(--border)',
                    borderRadius: '6px',
                    fontSize: '0.95rem'
                  }}
                />
              </div>

              <div
                style={{
                  display: 'flex',
                  justifyContent: 'flex-end',
                  gap: '0.75rem',
                  marginTop: '0.5rem'
                }}
              >
                <button
                  type="button"
                  onClick={() =>
                    setEditingCompany(null)
                  }
                  className="btn btn-outline"
                >
                  Cancel
                </button>

                <button
                  type="submit"
                  className="btn btn-primary"
                >
                  Save Changes
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
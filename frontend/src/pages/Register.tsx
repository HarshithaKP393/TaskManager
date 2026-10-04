import { useState, type FormEvent } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Register() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState<'ADMIN' | 'USER'>('USER');
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const { register } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError(null);
    setLoading(true);
    try {
      await register(email, password, role);
      navigate('/dashboard');
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Registration failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth">
      <div className="auth__panel">
        <h1 className="auth__title">Create your account</h1>
        <p className="auth__subtitle">Start tracking tasks in a minute.</p>
        <form onSubmit={handleSubmit}>
          <div className="field">
            <label htmlFor="email">Email</label>
            <input id="email" type="email" value={email} onChange={e => setEmail(e.target.value)} required />
          </div>
          <div className="field">
            <label htmlFor="password">Password</label>
            <input id="password" type="password" value={password} onChange={e => setPassword(e.target.value)} required minLength={6} />
          </div>
          <div className="field">
            <label htmlFor="role">Role</label>
            <select id="role" value={role} onChange={e => setRole(e.target.value as 'ADMIN' | 'USER')}>
              <option value="USER">USER</option>
              <option value="ADMIN">ADMIN</option>
            </select>
          </div>
          {error && <p className="form-error">{error}</p>}
          <button className="btn btn--primary" type="submit" disabled={loading} style={{ width: '100%' }}>
            {loading ? 'Creating account...' : 'Create account'}
          </button>
        </form>
        <p className="auth__footer">Already have an account? <Link to="/login">Log in</Link></p>
      </div>
    </div>
  );
}
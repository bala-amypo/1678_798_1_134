import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Form, Button, Card, Alert } from 'react-bootstrap';
import axios from 'axios';
import './Login.css';

const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        try {
            const response = await axios.post('http://localhost:8080/api/auth/login', {
                email,
                password
            });

            const { token, role, fullName } = response.data;
            
            // Store user data
            localStorage.setItem('token', token);
            localStorage.setItem('role', role);
            localStorage.setItem('email', email);
            localStorage.setItem('fullName', fullName);
            
            // Navigate based on role
            switch (role) {
                case 'ADMIN':
                    navigate('/admin/dashboard');
                    break;
                case 'CLINICIAN':
                    navigate('/clinician/dashboard');
                    break;
                case 'HEALTH_ASSISTANT':
                    navigate('/assistant/dashboard');
                    break;
                default:
                    navigate('/dashboard');
            }
        } catch (err) {
            setError(err.response?.data?.message || 'Login failed');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="login-container">
            <Card className="login-card">
                <Card.Body>
                    <h2 className="text-center mb-4">Post-Surgical Recovery System</h2>
                    {error && <Alert variant="danger">{error}</Alert>}
                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>Email</Form.Label>
                            <Form.Control
                                type="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                                placeholder="Enter email"
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Password</Form.Label>
                            <Form.Control
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                                placeholder="Enter password"
                            />
                        </Form.Group>
                        <Button 
                            variant="primary" 
                            type="submit" 
                            className="w-100"
                            disabled={loading}
                        >
                            {loading ? 'Logging in...' : 'Login'}
                        </Button>
                    </Form>
                    <div className="text-center mt-3">
                        <small>Demo Credentials:</small>
                        <div className="demo-credentials">
                            <div>Admin: admin@example.com / password</div>
                            <div>Clinician: clinician@example.com / password</div>
                        </div>
                    </div>
                </Card.Body>
            </Card>
        </div>
    );
};

export default Login;
import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { register } from "../services/authService";
import { useAuth } from "../hooks/useAuth";

export default function Register() {

    const navigate = useNavigate();
    const { login } = useAuth();

    const [form, setForm] = useState({
        name: "",
        email: "",
        password: "",
    });

    const [error, setError] = useState("");

    const handleChange = (e) => {

        setForm({
            ...form,
            [e.target.name]: e.target.value,
        });

    };

    const handleSubmit = async (e) => {

        e.preventDefault();
        setError("");

        try {

            const response = await register(form);

            login(response);

            navigate("/dashboard");

        } catch (err) {

            setError(
                err.response?.data?.message ||
                "Registration failed"
            );

        }

    };

    return (

        <div className="auth-container">

            <div className="auth-card">

                <h1 className="auth-title">

                    🚗 AutoHub

                </h1>

                <p className="auth-subtitle">

                    Create your account

                </p>

                <form onSubmit={handleSubmit}>

                    <input
                        className="input"
                        name="name"
                        placeholder="Name"
                        value={form.name}
                        onChange={handleChange}
                    />

                    <input
                        className="input"
                        name="email"
                        placeholder="Email"
                        value={form.email}
                        onChange={handleChange}
                    />

                    <input
                        className="input"
                        type="password"
                        name="password"
                        placeholder="Password"
                        value={form.password}
                        onChange={handleChange}
                    />

                    <button
                        className="btn btn-primary auth-btn"
                        type="submit"
                    >
                        Register
                    </button>

                </form>

                {error && (

                    <p className="auth-error">

                        {error}

                    </p>

                )}

                <p className="auth-footer">

                    Already have an account?

                    <Link to="/">

                        Login

                    </Link>

                </p>

            </div>

        </div>

    );

}
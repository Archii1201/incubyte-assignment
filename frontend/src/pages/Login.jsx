import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { login as loginUser } from "../services/authService";
import { useAuth } from "../hooks/useAuth";

export default function Login() {

    const navigate = useNavigate();
    const { login } = useAuth();

    const [form, setForm] = useState({
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

            const response = await loginUser(form);

            login(response);

            navigate("/dashboard");

        } catch (err) {

            setError(
                err.response?.data?.message ||
                "Login failed"
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
                    Login to continue
                </p>

                <form onSubmit={handleSubmit}>

                    <input
                        className="input"
                        type="email"
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
                        Login
                    </button>

                </form>

                {error && (

                    <p className="auth-error">

                        {error}

                    </p>

                )}

                <p className="auth-footer">

                    Don't have an account?

                    <Link to="/register">

                        Register

                    </Link>

                </p>

            </div>

        </div>

    );

}
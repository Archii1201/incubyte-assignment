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

        <div>

            <h1>Login</h1>

            <form onSubmit={handleSubmit}>

                <input
                    type="email"
                    name="email"
                    placeholder="Email"
                    value={form.email}
                    onChange={handleChange}
                />

                <br /><br />

                <input
                    type="password"
                    name="password"
                    placeholder="Password"
                    value={form.password}
                    onChange={handleChange}
                />

                <br /><br />

                <button type="submit">
                    Login
                </button>

            </form>

            {error && (
                <p style={{ color: "red" }}>
                    {error}
                </p>
            )}

            <p>
                Don't have an account?
                <Link to="/register"> Register</Link>
            </p>

        </div>
    );
}
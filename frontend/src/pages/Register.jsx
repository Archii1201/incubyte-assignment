import { useState } from "react";
import { useNavigate } from "react-router-dom";
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

        <div>

            <h1>Register</h1>

            <form onSubmit={handleSubmit}>

                <input
                    name="name"
                    placeholder="Name"
                    value={form.name}
                    onChange={handleChange}
                />

                <br /><br />

                <input
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
                    Register
                </button>

            </form>

            {error && (
                <p style={{ color: "red" }}>
                    {error}
                </p>
            )}

        </div>
    );
}
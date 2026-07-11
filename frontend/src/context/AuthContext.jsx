import { createContext, useEffect, useState } from "react";

export const AuthContext = createContext();

export function AuthProvider({ children }) {

  const [user, setUser] = useState(() => {

    const token = localStorage.getItem("token");
    const email = localStorage.getItem("email");
    const role = localStorage.getItem("role");

    if (token && email && role) {

        return {
            token,
            email,
            role,
        };
    }

    return null;
});

    const login = (data) => {

        localStorage.setItem("token", data.token);
        localStorage.setItem("email", data.email);
        localStorage.setItem("role", data.role);

        setUser(data);
    };

    const logout = () => {

        localStorage.clear();
        setUser(null);
    };

    return (

        <AuthContext.Provider
            value={{
                user,
                login,
                logout,
            }}
        >
            {children}
        </AuthContext.Provider>

    );
}
import { Routes, Route } from "react-router-dom";

import Login from "../pages/Login";
import Register from "../pages/Register";
import Dashboard from "../pages/Dashboard";
import ProtectedRoute from "../components/ProtectedRoute";
import AddVehicle from "../pages/AddVehicle";
import VehicleList from "../pages/VehicleList";
export default function AppRoutes() {

    return (

        <Routes>

            <Route
                path="/"
                element={<Login />}
            />

            <Route
                path="/register"
                element={<Register />}
            />

            <Route
                path="/dashboard"
                element={
                    <ProtectedRoute>
                        <Dashboard />
                    </ProtectedRoute>
                }
            />
            <Route
                path="/vehicles/add"
                element={
                    <ProtectedRoute>
                        <AddVehicle />
                    </ProtectedRoute>
                }
            />
            <Route

            path="/vehicles"

            element={

                <ProtectedRoute>

                    <VehicleList />

                </ProtectedRoute>

            }

        />

        </Routes>

    );
}
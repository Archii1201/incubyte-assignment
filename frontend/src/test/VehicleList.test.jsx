import { render, screen } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import { describe, test, expect, vi, beforeEach } from "vitest";

import VehicleList from "../pages/VehicleList";
import { getVehicles } from "../services/vehicleService";

vi.mock("../services/vehicleService", () => ({
    getVehicles: vi.fn(),
}));

describe("VehicleList", () => {

    beforeEach(() => {
        vi.clearAllMocks();
    });

    test("shows loading state", () => {

        getVehicles.mockImplementation(() => new Promise(() => {}));

        render(
            <BrowserRouter>
                <VehicleList />
            </BrowserRouter>
        );

        expect(
            screen.getByText(/loading vehicles/i)
        ).toBeInTheDocument();
    });

    test("shows empty state", async () => {

        getVehicles.mockResolvedValue([]);

        render(
            <BrowserRouter>
                <VehicleList />
            </BrowserRouter>
        );

        expect(
            await screen.findByText(/no vehicles found/i)
        ).toBeInTheDocument();
    });

    test("renders vehicle list", async () => {

        getVehicles.mockResolvedValue([
            {
                id: "1",
                make: "Toyota",
                model: "Corolla",
                price: 22000,
            },
        ]);

        render(
            <BrowserRouter>
                <VehicleList />
            </BrowserRouter>
        );

        expect(
            await screen.findByText("Toyota")
        ).toBeInTheDocument();

        expect(
            screen.getByText("Corolla")
        ).toBeInTheDocument();
    });

    test("shows error message", async () => {

        getVehicles.mockRejectedValue(
            new Error("Server error")
        );

        render(
            <BrowserRouter>
                <VehicleList />
            </BrowserRouter>
        );

        expect(
            await screen.findByText(/failed to load vehicles/i)
        ).toBeInTheDocument();
    });
});
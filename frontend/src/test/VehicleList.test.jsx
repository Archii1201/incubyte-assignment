import { render, screen } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import { describe, test, expect, vi, beforeEach } from "vitest";

import VehicleList from "../pages/VehicleList";
import {
    searchVehicles,
    deleteVehicle,
} from "../services/vehicleService";

vi.mock("../services/vehicleService", () => ({
    searchVehicles: vi.fn(),
    deleteVehicle: vi.fn(),
}));

describe("VehicleList", () => {

    beforeEach(() => {
        vi.clearAllMocks();
    });

    test("shows loading state", () => {

        searchVehicles.mockImplementation(
            () => new Promise(() => {})
        );

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

        searchVehicles.mockResolvedValue({
            content: [],
            totalPages: 0,
        });

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

        searchVehicles.mockResolvedValue({
            content: [
                {
                    id: "1",
                    make: "Toyota",
                    model: "Corolla",
                    category: "Sedan",
                    price: 22000,
                },
            ],
            totalPages: 1,
        });

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

        searchVehicles.mockRejectedValue(
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

    test("renders pagination buttons", async () => {

        searchVehicles.mockResolvedValue({
            content: [
                {
                    id: "1",
                    make: "Toyota",
                    model: "Corolla",
                    category: "Sedan",
                    price: 22000,
                },
            ],
            totalPages: 3,
        });

        render(
            <BrowserRouter>
                <VehicleList />
            </BrowserRouter>
        );

        await screen.findByText("Toyota");

        expect(
            screen.getByRole("button", {
                name: /previous/i,
            })
        ).toBeInTheDocument();

        expect(
            screen.getByRole("button", {
                name: /next/i,
            })
        ).toBeInTheDocument();
    });

});
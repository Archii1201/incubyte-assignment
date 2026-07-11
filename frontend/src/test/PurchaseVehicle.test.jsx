import { describe, test, expect, vi, beforeEach } from "vitest";
import { render, screen } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";

import VehicleList from "../pages/VehicleList";

import {
    searchVehicles,
    purchaseVehicle,
} from "../services/vehicleService";

vi.mock("../services/vehicleService", () => ({
    searchVehicles: vi.fn(),
    purchaseVehicle: vi.fn(),
}));

describe("Purchase Vehicle", () => {

    beforeEach(() => {

        vi.clearAllMocks();

        localStorage.setItem("role", "USER");
    });

    test("shows purchase button for available vehicle", async () => {

        searchVehicles.mockResolvedValue({
            content: [
                {
                    id: "1",
                    make: "Toyota",
                    model: "Corolla",
                    price: 22000,
                    quantity: 5,
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
            await screen.findByText("Purchase")
        ).toBeInTheDocument();
    });
    test("purchase cancelled does not call API", async () => {

    window.confirm = vi.fn(() => false);

    searchVehicles.mockResolvedValue({
        content: [
            {
                id: "1",
                make: "Toyota",
                model: "Corolla",
                price: 22000,
                quantity: 5,
            },
        ],
        totalPages: 1,
    });

    render(
        <MemoryRouter>
            <VehicleList />
        </MemoryRouter>
    );

    fireEvent.click(
        await screen.findByText("Purchase")
    );

    expect(purchaseVehicle).not.toHaveBeenCalled();
});
test("purchase cancelled does not call API", async () => {

    window.confirm = vi.fn(() => false);

    searchVehicles.mockResolvedValue({
        content: [
            {
                id: "1",
                make: "Toyota",
                model: "Corolla",
                price: 22000,
                quantity: 5,
            },
        ],
        totalPages: 1,
    });

    render(
        <MemoryRouter>
            <VehicleList />
        </MemoryRouter>
    );

    fireEvent.click(
        await screen.findByText("Purchase")
    );

    expect(purchaseVehicle).not.toHaveBeenCalled();
});
});
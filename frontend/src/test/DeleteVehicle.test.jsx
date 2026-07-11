import { describe, test, expect, vi, beforeEach } from "vitest";
import { render, screen, waitFor, fireEvent } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import VehicleList from "../pages/VehicleList";
import * as vehicleService from "../services/vehicleService";

vi.mock("../services/vehicleService");

describe("Delete Vehicle", () => {

    beforeEach(() => {

        vi.clearAllMocks();

        localStorage.setItem("role", "ADMIN");
    });

    test("shows delete button for admin", async () => {

        vehicleService.getVehicles.mockResolvedValue([
            {
                id: "1",
                make: "Toyota",
                model: "Corolla",
                price: 22000
            }
        ]);

        render(
            <MemoryRouter>
                <VehicleList />
            </MemoryRouter>
        );

        expect(
            await screen.findByText("Delete")
        ).toBeInTheDocument();
    });

    test("clicking delete opens confirmation", async () => {

        window.confirm = vi.fn(() => false);

        vehicleService.getVehicles.mockResolvedValue([
            {
                id: "1",
                make: "Toyota",
                model: "Corolla",
                price: 22000
            }
        ]);

        render(
            <MemoryRouter>
                <VehicleList />
            </MemoryRouter>
        );

        fireEvent.click(
            await screen.findByText("Delete")
        );

        expect(window.confirm).toHaveBeenCalled();
    });

    test("confirmed delete calls API", async () => {

        window.confirm = vi.fn(() => true);

        vehicleService.getVehicles.mockResolvedValue([
            {
                id: "1",
                make: "Toyota",
                model: "Corolla",
                price: 22000
            }
        ]);

        vehicleService.deleteVehicle.mockResolvedValue();

        render(
            <MemoryRouter>
                <VehicleList />
            </MemoryRouter>
        );

        fireEvent.click(
            await screen.findByText("Delete")
        );

        await waitFor(() => {

            expect(vehicleService.deleteVehicle)
                    .toHaveBeenCalledWith("1");
        });
    });

    test("reloads vehicles after delete", async () => {

        window.confirm = vi.fn(() => true);

        vehicleService.getVehicles.mockResolvedValue([
            {
                id: "1",
                make: "Toyota",
                model: "Corolla",
                price: 22000
            }
        ]);

        vehicleService.deleteVehicle.mockResolvedValue();

        render(
            <MemoryRouter>
                <VehicleList />
            </MemoryRouter>
        );

        fireEvent.click(
            await screen.findByText("Delete")
        );

        await waitFor(() => {

            expect(vehicleService.getVehicles)
                    .toHaveBeenCalledTimes(2);
        });
    });

});
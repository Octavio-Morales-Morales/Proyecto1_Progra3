package Repositorios;

import Dominio.Ventanillas;
import java.util.List;
import java.util.Optional;

public interface InterfaceVentanilla {
    void cambiarEstado(int ventanillaId, String estado);

    List<Integer> ListaVentanillasActivas();

    void crear(Ventanillas ventanilla);

    void actualizar(Ventanillas ventanilla);

    Optional<Ventanillas> buscarPorId(int ventanillaId);

    Optional<Ventanillas> buscarPorNumero(int numero);

    List<Ventanillas> listar();
}

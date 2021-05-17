package site.bitlab16;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Before;

public class GraphicTest {

	private Graphic graphic;

	@Before
	public void setup() {
		this.graphic = new Graphic(new BasicSimulator(), "");
	}

	//
	// non è possibile testare un interfaccia grafica in CI/CD,
	// inoltre è una classe per il develop e la visualizzazione grafica
	// dei dati, non entra in produzione
	//


}

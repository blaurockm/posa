package net.buchlese.verw.reports;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;
import org.apache.xmlgraphics.io.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class FopFactoryComponent {

	private final FopFactory fopFactory;
	
	public FopFactoryComponent() {
		FopFactoryBuilder builder = new FopFactoryBuilder(new File(".").toURI(), new VerwResourceResolver());
		builder.setSourceResolution(96);
		fopFactory = builder.build();
	}
	
	
	public FOUserAgent newFOUserAgent() {
		return fopFactory.newFOUserAgent();
	}
	
	private static class VerwResourceResolver implements org.apache.xmlgraphics.io.ResourceResolver {

		@Override
		public Resource getResource(URI uri) throws IOException {
			if (uri.getScheme() == null || uri.getScheme().equals("classpath") ) {
				return new Resource(new ClassPathResource(uri.getPath()).getInputStream());
			}
			return new Resource(uri.toURL().openStream());
		}

		@Override
		public OutputStream getOutputStream(URI uri) throws IOException {
			return new FileOutputStream(new File(uri));
		}

	}

}

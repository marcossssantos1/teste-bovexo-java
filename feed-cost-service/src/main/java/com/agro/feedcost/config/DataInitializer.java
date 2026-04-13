package com.agro.feedcost.config;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.agro.feedcost.entity.FeedCost;
import com.agro.feedcost.entity.FeedType;
import com.agro.feedcost.repository.FeedCostRepository;

@Configuration
public class DataInitializer {

	private static final Logger log = Logger.getLogger(DataInitializer.class.getName());

	@Bean
	public CommandLineRunner initData(FeedCostRepository repository) {
		return args -> {
			if (repository.count() > 0) {
				log.info("Dados de custo já existem, pulando inicialização.");
				return;
			}

			log.info("Populando tabela feed_cost com dados iniciais...");

			List<FeedCost> custos = List.of(criarCusto(FeedType.MILHO, new BigDecimal("2.50")),
					criarCusto(FeedType.SOJA, new BigDecimal("3.80")),
					criarCusto(FeedType.FARELO_SOJA, new BigDecimal("4.20")),
					criarCusto(FeedType.SORGO, new BigDecimal("2.10")),
					criarCusto(FeedType.TRIGO, new BigDecimal("2.90")),
					criarCusto(FeedType.SUPLEMENTO_MINERAL, new BigDecimal("5.50")),
					criarCusto(FeedType.NUCLEO_PROTEICO, new BigDecimal("6.75")),
					criarCusto(FeedType.SAL_BRANCO, new BigDecimal("1.20")),
					criarCusto(FeedType.UREIA, new BigDecimal("3.30")),
					criarCusto(FeedType.SILAGEM_MILHO, new BigDecimal("1.80")));

			repository.saveAll(custos);
			log.info("Dados iniciais inseridos com sucesso.");
		};
	}

	private FeedCost criarCusto(FeedType feedType, BigDecimal costPerKg) {
		FeedCost feedCost = new FeedCost();
		feedCost.setFeedType(feedType);
		feedCost.setCostPerKg(costPerKg);
		return feedCost;
	}

}

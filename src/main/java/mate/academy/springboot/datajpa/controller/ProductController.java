package mate.academy.springboot.datajpa.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import mate.academy.springboot.datajpa.dto.mapper.RequestDtoMapper;
import mate.academy.springboot.datajpa.dto.mapper.ResponseDtoMapper;
import mate.academy.springboot.datajpa.dto.request.ProductRequestDto;
import mate.academy.springboot.datajpa.dto.response.ProductResponseDto;
import mate.academy.springboot.datajpa.model.Product;
import mate.academy.springboot.datajpa.service.ProductService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final ResponseDtoMapper<ProductResponseDto, Product> responseDtoMapper;
    private final RequestDtoMapper<ProductRequestDto, Product> requestDtoMapper;

    public ProductController(ProductService productService,
                             ResponseDtoMapper<ProductResponseDto, Product> responseDtoMapper,
                             RequestDtoMapper<ProductRequestDto, Product> requestDtoMapper) {
        this.productService = productService;
        this.responseDtoMapper = responseDtoMapper;
        this.requestDtoMapper = requestDtoMapper;
    }

    @PostMapping
    public ProductResponseDto create(@RequestBody ProductRequestDto productRequestDto) {
        Product product = productService.save(requestDtoMapper.toModel(productRequestDto));
        return responseDtoMapper.toResponseDto(product);
    }

    @GetMapping("/{id}")
    public ProductResponseDto get(@PathVariable Long id) {
        return responseDtoMapper.toResponseDto(productService.get(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }

    @PutMapping("/{id}")
    public ProductResponseDto update(@PathVariable Long id,
                                     @RequestBody ProductRequestDto requestDto) {
        Product product = requestDtoMapper.toModel(requestDto);
        product.setId(id);
        return responseDtoMapper.toResponseDto(productService.update(product));
    }

    @GetMapping("/by-price")
    public List<ProductResponseDto> getAllByPriceBetween(@RequestParam BigDecimal from,
                                                         @RequestParam BigDecimal to) {
        return productService.findAllByPriceBetween(from, to).stream()
                .map(responseDtoMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping
    public List<ProductResponseDto> getAllBy(@RequestParam Map<String, String> params) {
        return productService.findAll(params).stream()
                .map(responseDtoMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
